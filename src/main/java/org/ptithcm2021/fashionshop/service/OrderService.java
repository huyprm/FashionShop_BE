//package org.ptithcm2021.fashionshop.service;
//
//import lombok.RequiredArgsConstructor;
//import org.ptithcm2021.fashionshop.dto.request.OrderRequest;
//import org.ptithcm2021.fashionshop.dto.response.OrderResponse;
//import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
//import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;
//import org.ptithcm2021.fashionshop.enums.PaymentStatusEnum;
//import org.ptithcm2021.fashionshop.exception.AppException;
//import org.ptithcm2021.fashionshop.exception.ErrorCode;
//import org.ptithcm2021.fashionshop.mapper.OrderMapper;
//import org.ptithcm2021.fashionshop.model.*;
//import org.ptithcm2021.fashionshop.repository.BundleDiscountRepository;
//import org.ptithcm2021.fashionshop.repository.OrderRepository;
//import org.ptithcm2021.fashionshop.repository.ProductVariantRepository;
//import org.ptithcm2021.fashionshop.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//    private final OrderRepository orderRepository;
//    private final OrderMapper orderMapper;
//    private final UserRepository userRepository;
//    private final ProductVariantRepository productVariantRepository;
//    private final BundleDiscountRepository bundleDiscountRepository;
//
//    public OrderResponse createOrder(OrderRequest orderRequest, int paymentStatus) {
//        Order order = orderMapper.toOrder(orderRequest);
//
//        User user = userRepository.findById(orderRequest.getUser_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        order.setUser(user);
//
//        switch (paymentStatus) {
//            case 1: {
//                order.setPaymentStatus(PaymentStatusEnum.PAID);
//                order.setStatus(OrderStatusEnum.CONFIRMED);
//                break;
//            }
//            case -1:{
//                order.setPaymentStatus(PaymentStatusEnum.PENDING);
//                break;
//            }
//            case 0:{
//                order.setPaymentStatus(PaymentStatusEnum.FAILED);
//                break;
//            }
//        }
//
//        double total_price = 0;
//        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream().map(request ->{
//            OrderDetail orderDetail = new OrderDetail();
//
//            ProductVariant productVariant = productVariantRepository.findById(request.getProductVariant_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
//            orderDetail.setProductVariant(productVariant);
//            orderDetail.setQuantity(request.getQuantity());
//
//            BundleDiscount bundleDiscount = bundleDiscountRepository
//                    .findById(request.getBundleDiscount_id()).orElseThrow(()-> new RuntimeException("BundleDiscount not found"));
//
//            if(bundleDiscount.getBundledProduct().getId() == productVariant.getProduct().getId());
//
//        }).toList();
//
//
//    }
//}
