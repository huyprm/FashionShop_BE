package org.ptithcm2021.fashionshop.service;//package org.ptithcm2021.fashionshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.request.CartUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.BundleDiscountResponse;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.CartMapper;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.BundleDiscountRepository;
import org.ptithcm2021.fashionshop.repository.CartRepository;
import org.ptithcm2021.fashionshop.repository.ProductVariantRepository;
import org.ptithcm2021.fashionshop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private BundleDiscountRepository bundleDiscountRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    private CartRequest cartRequest;
    private CartUpdateRequest cartUpdateRequest;
    private User user;
    private ProductVariant productVariant;
    private Cart cart;
    private BundleDiscount bundleDiscount;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");
        user.setFullname("john_doe");

        bundleDiscount = BundleDiscount.builder()
                .id(201)
                .discountType(DiscountTypeEnum.PERCENTAGE)
                .discountValue(10.0)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .mainProduct(product)
                .build();

        List<BundleDiscount> bundleDiscountList =new ArrayList<>();
        bundleDiscountList.add(bundleDiscount);

        product = new Product();
        product.setId(1001);
        product.setBundleDiscountList(bundleDiscountList);

        cartRequest = new CartRequest(
                "user123", 3, Arrays.asList(new CartRequest.DiscountDetailRequest(101, 201, 2)), 101);

        productVariant = new ProductVariant();
        productVariant.setId(101);
        productVariant.setPrice(20.0);
        productVariant.setProduct(product);

        // Mock CartDiscountDetail
        CartDiscountDetail cartDiscountDetail = CartDiscountDetail.builder()
                .id(301)
                .quantity(2)
                .price(30.0) // Giá 30.0 cho 2 sản phẩm
                .productVariant(productVariant)
                .build();

        // Mock Cart
        cart = Cart.builder()
                .id(1)
                .quantity(3)
                .totalPrice(90.0)
                .productVariant(productVariant)
                .cartDiscountDetails(List.of(cartDiscountDetail))
                .build();

        // Mock CartUpdateRequest
        Map<Integer, Integer> productDiscount = new HashMap<>();
        productDiscount.put(301, 3); // Cập nhật số lượng từ 2 lên 3
        cartUpdateRequest = new CartUpdateRequest(1, 4, "user123", productDiscount);
    }

    @Test
    void testCreateCart_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(productVariantRepository.findById(101)).thenReturn(Optional.of(productVariant));
        when(bundleDiscountRepository.findById(201)).thenReturn(Optional.of(bundleDiscount));
        when(cartRepository.findByUserIdAndProductVariantId("user123", 101)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(new CartResponse());

        CartResponse response = cartService.createCart(cartRequest);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testCreateCart_UserNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            cartService.createCart(cartRequest);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateCart_ProductNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(productVariantRepository.findById(101)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            cartService.createCart(cartRequest);
        });

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetCartByUserId_Success() {
        when(cartRepository.findByUserId("user123")).thenReturn(Optional.of(java.util.List.of(cart)));
        when(cartMapper.toCartResponse(cart)).thenReturn(new CartResponse());

        var carts = cartService.getCartByUserId("user123");

        assertFalse(carts.isEmpty());
        verify(cartRepository, times(1)).findByUserId("user123");
    }

    @Test
    //@WithMockUser(username = "user123")
    void testGetCartByUserId_NotFound() {
        when(cartRepository.findByUserId("user123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.getCartByUserId("user123");
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    //@WithMockUser(username = "user123")
    void testDeleteCart_Main_Success() {
        when(cartRepository.existsById(1)).thenReturn(true);
        doNothing().when(cartRepository).deleteById(1);

        String result = cartService.deleteCart(1, "main");

        assertEquals("Cart deleted", result);
        verify(cartRepository, times(1)).deleteById(1);
    }

    @Test
    //@WithMockUser(username = "user123")
    void testDeleteCart_Main_NotFound() {
        when(cartRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteCart(1, "main");
        });

        assertEquals("Cart with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testUpdateCart_CartNotFound(){
        when(cartRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cartService.updateCart(cartUpdateRequest));

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testUpdateCart_Successful(){
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toCartResponse(any())).thenReturn(new CartResponse());

        CartResponse response = cartService.updateCart(cartUpdateRequest);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCart() {
        // Giả lập behavior của cartRepository
        when(cartRepository.findById(cartUpdateRequest.getCartId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Giả lập behavior của cartMapper
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(new CartResponse());

        // Gọi hàm updateCart
        CartResponse response = cartService.updateCart(cartUpdateRequest);

        // Kiểm tra số lượng được cập nhật đúng
        assertEquals(4, cart.getQuantity()); // Tổng quantity cập nhật đúng
        assertEquals(3, cart.getCartDiscountDetails().get(0).getQuantity()); // Số lượng discount cập nhật đúng

        // Kiểm tra tổng giá tiền được tính lại
        double expectedTotalPrice = (3 * (30.0 / 2)) + (4 * 20.0); // Giá sau khi cập nhật
        assertEquals(expectedTotalPrice, cart.getTotalPrice());

        // Kiểm tra xem có gọi save không
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}


