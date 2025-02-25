package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.CartMapper;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final BundleDiscountRepository bundleDiscountRepository;
    private final CartMapper cartMapper;

    public CartResponse createCart(CartRequest cartRequest) {
        Cart cart = new Cart();

        User user = userRepository.findById(cartRequest.getUser_id()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        cart.setUser(user);

        ProductVariant productVariant = productVariantRepository
                .findById(cartRequest.getProductVariant_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        cart.setProductVariant(productVariant);
        cart.setQuantity(cartRequest.getQuantity());

        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);
        List<CartDiscountDetail> cartDiscountDetails =
                Optional.ofNullable(cartRequest.getCartDiscountDetails()).orElse(Collections.emptyList())
                .stream().map(request->{
            CartDiscountDetail cartDiscountDetail = new CartDiscountDetail();
            cartDiscountDetail.setQuantity(request.getQuantity());

            ProductVariant productDiscount = productVariantRepository
                    .findById(request.getProductVariant_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            BundleDiscount bundleDiscount = bundleDiscountRepository
                    .findById(request.getBundleDiscount_id()).orElseThrow(() -> new RuntimeException("Bundle discount not found"));

            if(bundleDiscount.getStartDate().isBefore(LocalDateTime.now()) || bundleDiscount.getEndDate().isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Discount expired.");
            }

            if(productVariant.getProduct().getId() == bundleDiscount.getMainProduct().getId()){

                if(bundleDiscount.getMainProduct().getId() != productDiscount.getProduct().getId())
                    throw new RuntimeException("Product variant is not the same as the product variant in the bundle discount");
            }
            else{
                throw new RuntimeException("Product variant is not the same as the product variant in the bundle discount");
            }

            double price = bundleDiscount.getDiscountType() == DiscountTypeEnum.PERCENTAGE ? productDiscount.getPrice() * request.getQuantity() * bundleDiscount.getDiscountValue() / 100
                    : productDiscount.getPrice() * request.getQuantity() - bundleDiscount.getDiscountValue();
            totalPrice.set(totalPrice.get() + price);
            cartDiscountDetail.setPrice(price);
            cartDiscountDetail.setBundleDiscount(bundleDiscount);
            cartDiscountDetail.setProductVariant(productDiscount);
            cartDiscountDetail.setCart(cart);
            return cartDiscountDetail;
        }).toList();

        totalPrice.set(totalPrice.get() + cartRequest.getQuantity() * productVariant.getPrice());
        cart.setTotalPrice(totalPrice.get());

        log.info("Create cart {}", totalPrice.get());
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }
}
