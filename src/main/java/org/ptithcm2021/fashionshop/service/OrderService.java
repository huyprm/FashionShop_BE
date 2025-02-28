package org.ptithcm2021.fashionshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.OrderRequest;
import org.ptithcm2021.fashionshop.dto.response.OrderResponse;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.OrderMapper;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final VoucherRepository voucherRepository;

    @Transactional
    @PreAuthorize("#id == authentication.name")
    public OrderResponse createOrder(OrderRequest orderRequest, String id){

        Cart cart = cartRepository.findById(orderRequest.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Order order = orderMapper.toOrder(orderRequest);

        if(orderRequest.getVoucherCode() != null) {
            Voucher voucher = voucherRepository.findByCode(orderRequest.getVoucherCode())
                    .orElseThrow(() -> new RuntimeException("Invalid voucher code"));

            if (voucher.getMinOrderValue() > cart.getTotalPrice()) {
                throw new RuntimeException("You are not eligible for discount");
            }
            if (voucher.getQuantity() > 0) {
                if (voucher.getEndDate().isBefore(orderRequest.getDate())) {
                    throw new RuntimeException("Voucher expired");
                }
            } else {
                throw new RuntimeException("Coupon has been used up");
            }

            double discount;
            if (voucher.getDiscountType() == DiscountTypeEnum.PERCENTAGE) {
                double percentageDiscount = cart.getTotalPrice() * voucher.getDiscountValue() / 100;
                Double maxValue = voucher.getMaxDiscountValue();
                discount = (maxValue != null) ? Math.min(percentageDiscount, maxValue) : percentageDiscount;
            } else {
                discount = voucher.getDiscountValue();
            }

            order.setVoucherDiscount_price(cart.getTotalPrice() - discount);
        }

        List<OrderDetail> orderDetails = new ArrayList<>();

        orderDetails.add(OrderDetail.builder()
                .productVariant(cart.getProductVariant())
                .quantity(cart.getQuantity())
                .price(cart.getProductVariant().getPrice())
                .order(order)
                .build());

        if(cart.getCartDiscountDetails() != null) {
            cart.getCartDiscountDetails().forEach(detail -> {
                orderDetails.add(OrderDetail.builder()
                        .order(order)
                        .productVariant(detail.getProductVariant())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build());
            });
        }

        order.setUser(user);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setTotal_price(cart.getTotalPrice()-order.getVoucherDiscount_price());
        order.setOrderDetails(orderDetails);

        cartRepository.delete(cart);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }


}
