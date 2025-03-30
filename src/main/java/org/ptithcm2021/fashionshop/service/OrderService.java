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
    public OrderResponse createOrder(OrderRequest orderRequest, String id) {
        List<Cart> carts= new ArrayList<>();
        orderRequest.getCartId().forEach(integer -> {
            Cart cart = cartRepository.findById(integer)
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

            carts.add(cart);
        });

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Order order = orderMapper.toOrder(orderRequest);
        order.setUser(user);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setVoucherDiscount_price(0);


        processVoucher(orderRequest, carts, order);

        List<OrderDetail> orderDetails = createOrderDetails(carts, order);
        updateInventory(orderDetails);

        double total = carts.stream().mapToDouble(Cart::getTotalPrice).sum();
        order.setTotal_price(total - order.getVoucherDiscount_price());
        order.setOrderDetails(orderDetails);

        cartRepository.deleteAll(carts);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    private void processVoucher(OrderRequest orderRequest, List<Cart> carts, Order order) {
        if (orderRequest.getVoucherCode() == null) {
            return;
        }

        carts.forEach(cart -> {
        Voucher voucher = voucherRepository.findByCode(orderRequest.getVoucherCode())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_VOUCHER));

        if (voucher.getMinOrderValue() > cart.getTotalPrice()) {
            throw new AppException(ErrorCode.INELIGIBLE_FOR_DISCOUNT);
        }

        if (voucher.getQuantity() <= 0) {
            throw new AppException(ErrorCode.VOUCHER_DEPLETED);
        }

        if (voucher.getEndDate().isBefore(orderRequest.getDate())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        double discount = calculateDiscount(voucher, cart.getTotalPrice());
        order.setVoucherDiscount_price(order.getVoucherDiscount_price() +discount);
    });
    }


    private double calculateDiscount(Voucher voucher, double totalPrice) {
        if (voucher.getDiscountType() == DiscountTypeEnum.PERCENTAGE) {
            double percentageDiscount = totalPrice * voucher.getDiscountValue() / 100;
            Double maxValue = voucher.getMaxDiscountValue();
            return (maxValue != null) ? Math.min(percentageDiscount, maxValue) : percentageDiscount;
        } else {
            return voucher.getDiscountValue();
        }
    }

    private List<OrderDetail> createOrderDetails(List<Cart> carts, Order order) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        carts.forEach(cart -> {
            orderDetails.add(OrderDetail.builder()
                    .productVariant(cart.getProductVariant())
                    .quantity(cart.getQuantity())
                    .price(cart.getProductVariant().getPrice())
                    .order(order)
                    .build());

            if (cart.getCartDiscountDetails() != null) {
                cart.getCartDiscountDetails().forEach(detail -> {
                    orderDetails.add(OrderDetail.builder()
                            .order(order)
                            .productVariant(detail.getProductVariant())
                            .quantity(detail.getQuantity())
                            .price(detail.getPrice())
                            .build());
                });
            }
        });
        return orderDetails;
    }

    private void updateInventory(List<OrderDetail> orderDetails) {
        orderDetails.forEach(orderDetail -> {
            ProductVariant variant = orderDetail.getProductVariant();
            int quantity = orderDetail.getQuantity();

            variant.setQuantity(variant.getQuantity() - quantity);
            variant.getProduct().setStock_quantity(
                    variant.getProduct().getStock_quantity() - quantity);
        });
    }
}