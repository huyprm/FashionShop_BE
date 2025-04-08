package org.ptithcm2021.fashionshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
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
import org.ptithcm2021.fashionshop.service.CartService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CartServiceTest1 {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BundleDiscountRepository bundleDiscountRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    private User user;
    private ProductVariant productVariant;
    private Cart cart;
    private BundleDiscount bundleDiscount;
    private CartRequest cartRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");
        user.setFullname("john_doe");

        Product product = new Product();
        product.setId(1001);
        product.setName("Red T-Shirt");

        productVariant = new ProductVariant();
        productVariant.setId(101);
        productVariant.setProduct(product);
        productVariant.setPrice(19.99);

        bundleDiscount = new BundleDiscount();
        bundleDiscount.setId(201);
        bundleDiscount.setDiscountType(DiscountTypeEnum.PERCENTAGE);
        bundleDiscount.setDiscountValue(10);
        bundleDiscount.setStartDate(LocalDateTime.now().minusDays(1));
        bundleDiscount.setEndDate(LocalDateTime.now().plusDays(1));

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setProductVariant(productVariant);
        cart.setQuantity(3);
        cart.setTotalPrice(59.97);

        cartRequest = new CartRequest("user123", 3, null, 101);
    }

    @Test
    void testCreateCart_Success() {
        // Giả lập dữ liệu từ repository
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(productVariantRepository.findById(101)).thenReturn(Optional.of(productVariant));
        when(cartRepository.findByUserIdAndProductVariantId("user123", 101)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(new CartResponse(1, 5997, 3, productVariant, null));

        // Gọi service
        CartResponse response = cartService.createCart(cartRequest);

        // Kiểm tra kết quả
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(3, response.getQuantity());
        assertEquals(5997.0, response.getTotalPrice());
    }

    @Test
    void testCreateCart_UserNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> cartService.createCart(cartRequest));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateCart_ProductVariantNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(productVariantRepository.findById(101)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> cartService.createCart(cartRequest));
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateCart_WithBundleDiscount() {
        // Mock User và ProductVariant chính
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(productVariantRepository.findById(101)).thenReturn(Optional.of(productVariant));

        // Mock Bundle Discount
        ProductVariant bundledProduct = new ProductVariant();
        bundledProduct.setId(102);
        bundledProduct.setProduct(new Product());
        bundledProduct.setPrice(25.0);

        CartDiscountDetail cartDiscountDetail = new CartDiscountDetail();
        cartDiscountDetail.setProductVariant(bundledProduct);
        cartDiscountDetail.setQuantity(1);
        cartDiscountDetail.setPrice(22.5); // 10% giảm giá

        when(productVariantRepository.findById(102)).thenReturn(Optional.of(bundledProduct));
        when(bundleDiscountRepository.findById(201)).thenReturn(Optional.of(bundleDiscount));

        // Mock cart đã tồn tại với sản phẩm chính
        Cart existingCart = new Cart();
        existingCart.setId(1);
        existingCart.setUser(user);
        existingCart.setProductVariant(productVariant);
        existingCart.setQuantity(2);
        existingCart.setTotalPrice(39.98); // (2 * 19.99)

        when(cartRepository.findByUserIdAndProductVariantId("user123", 101)).thenReturn(Optional.of(existingCart));

        // Mock danh sách giảm giá
        List<CartRequest.DiscountDetailRequest> discountRequests = List.of(
                new CartRequest.DiscountDetailRequest(102, 201, 1)
        );

        CartRequest cartRequestWithDiscount = new CartRequest("user123", 1, discountRequests, 101);

        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            return cartMapper.toCartResponse(savedCart);
        });

        // Gọi service
        CartResponse response = cartService.createCart(cartRequestWithDiscount);

        // Kiểm tra kết quả
        assertNotNull(response);
        assertEquals(3, response.getQuantity()); // 2 (trước đó) + 1 (mới)
        assertEquals(62.48, response.getTotalPrice()); // 39.98 + 22.5
    }

}

