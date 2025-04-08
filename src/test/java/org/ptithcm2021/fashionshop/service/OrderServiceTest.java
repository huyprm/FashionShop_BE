package org.ptithcm2021.fashionshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.OrderRequest;
import org.ptithcm2021.fashionshop.dto.response.OrderResponse;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.OrderMapper;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.CartRepository;
import org.ptithcm2021.fashionshop.repository.OrderRepository;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderStatusEnum status;
    private User user;
    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;
    private Order order5;
    private Order order6;
    private OrderResponse response1;
    private OrderResponse response3;
    private String userId;
    private Cart cart;
    private Product product;
    private Order order;
    private OrderRequest request;
    private ProductVariant variant;
    private OrderResponse response;

    @BeforeEach()
    void setup(){
        userId = "user123";
        status = OrderStatusEnum.DELIVERED;

        user = new User();
        user.setId(userId);

        order1 = new Order();
        order1.setStatus(OrderStatusEnum.DELIVERED);

        order2 = new Order();
        order2.setStatus(OrderStatusEnum.PENDING);

        order3 = new Order();
        order3.setStatus(OrderStatusEnum.DELIVERED);

        order4 = new Order();
        order4.setStatus(OrderStatusEnum.DELIVERING);

        order5 = new Order();
        order5.setStatus(OrderStatusEnum.CANCELLED);

        order6 = new Order();
        order6.setStatus(OrderStatusEnum.PENDING);

        user.setOrders(List.of(order1, order2, order3));

        response1 = new OrderResponse();
        response3 = new OrderResponse();

        // Prepare request
        request = new OrderRequest();
        request.setPaymentMethod(PaymentMethodEnum.COD);
        request.setVoucherCode(null); // no voucher
        request.setDate(LocalDateTime.now());
        request.setCartId(List.of(1)); // 1 cart

        // Cart and product variant
        product = new Product();
        product.setStock_quantity(10);

        variant = new ProductVariant();
        variant.setId(1);
        variant.setPrice(100.0);
        variant.setQuantity(5); // current inventory
        variant.setProduct(product);

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setQuantity(2);
        cart.setProductVariant(variant);

        // Order mapping
        order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatusEnum.PENDING);
        order.setVoucherDiscount_price(0);
        order.setTotal_price(200);

        response = new OrderResponse();
    }

    @Test
    void testGetOrderByStatus_ShouldReturnOrders() {
        // WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderMapper.toOrderResponse(order1)).thenReturn(response1);
        when(orderMapper.toOrderResponse(order3)).thenReturn(response3);

        List<OrderResponse> result = orderService.getOrderByStatus(userId, OrderStatusEnum.DELIVERED);

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.contains(response1));
        assertTrue(result.contains(response3));
        verify(userRepository).findById(userId);
        verify(orderMapper).toOrderResponse(order1);
        verify(orderMapper).toOrderResponse(order3);
    }

    @Test
    void testCreateOrder_Success() {

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderMapper.toOrder(any(OrderRequest.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toOrderResponse(order)).thenReturn(response);

        // WHEN
        OrderResponse result = orderService.createOrder(request, userId);

        // THEN
        assertNotNull(result);
        verify(cartRepository).deleteAll(any());
        verify(orderRepository).save(any(Order.class));
    }
    @Test
    void testCreateOrder_ShouldThrowCartNotFound_WhenCartIdInvalid() {
        // GIVEN
        String userId = "user123";
        OrderRequest request = new OrderRequest();
        request.setCartId(List.of(99)); // cartId không tồn tại
        request.setDate(LocalDateTime.now());

        when(cartRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN - THEN
        AppException exception = assertThrows(AppException.class, () -> {
            orderService.createOrder(request, userId);
        });

        assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
        verify(cartRepository).findById(99);
    }

    @Test
    void testCreateOrder_ShouldThrowOutOfStock_WhenQuantityExceedsInventory() {
        String userId = "user123";
        OrderRequest request1 = new OrderRequest();
        request1.setCartId(List.of(2));
        request1.setDate(LocalDateTime.now());


        Cart cart = new Cart();
        cart.setId(2);
        cart.setQuantity(10); // yêu cầu mua 5 sản phẩm
        cart.setProductVariant(variant);

        when(cartRepository.findById(2)).thenReturn(Optional.of(cart));

        // WHEN - THEN
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(request1, userId);
        });

        assertEquals("Out of stock", ex.getMessage());
    }

    @Test
    void testCancelOrder_ShouldCancelSuccessfully() {
        // GIVEN
        long orderId = 1L;
        String userId = "user123";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatusEnum.PENDING); // Trạng thái ban đầu

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // WHEN
        orderService.cancelOrder(orderId, userId);

        // THEN
        assertEquals(OrderStatusEnum.CANCELLED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testCancelOrder_ShouldThrowException_WhenOrderNotFound() {
        // GIVEN
        long orderId = 999L;
        String userId = "user123";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // WHEN - THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(orderId, userId);
        });

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void testGetOrderByStatus_ShouldReturnListOfOrderResponses() {
        // GIVEN
        String userId = "user123";
        OrderStatusEnum status = OrderStatusEnum.PENDING;

        List<Order> orders = List.of(order1, order2, order3, order4, order5, order6);
        user.setOrders(orders);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderMapper.toOrderResponse(any(Order.class))).thenReturn(new OrderResponse());

        // WHEN
        List<OrderResponse> responses = orderService.getOrderByStatus(userId, status);

        // THEN
        assertEquals(2, responses.size()); // chỉ lấy order1 và order3
        verify(orderMapper, times(2)).toOrderResponse(any(Order.class));
    }

    @Test
    void testGetOrderByStatus_ShouldThrowException_WhenUserNotFound() {
        // GIVEN
        String userId = "user123";
        OrderStatusEnum status = OrderStatusEnum.PENDING;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN - THEN
        AppException exception = assertThrows(AppException.class, () -> {
            orderService.getOrderByStatus(userId, status);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


}
