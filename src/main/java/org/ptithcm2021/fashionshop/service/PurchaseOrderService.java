package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.PurchaseOrderRequest;
import org.ptithcm2021.fashionshop.dto.response.PurchaseOrderResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.forEach;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;


    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) {
        User user = userRepository.findById(purchaseOrderRequest.getUser_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Supplier supplier = supplierRepository.findById(purchaseOrderRequest.getSupplier_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PurchaseOrder purchaseOrder = toPurchaseOrder(purchaseOrderRequest, user, supplier);

        List<PurchaseOrderDetail> purchaseOrderDetails = toPurchaseOrderDetails(purchaseOrderRequest, purchaseOrder);

        purchaseOrder.setPurchaseOrderDetails(purchaseOrderDetails);
        purchaseOrderRepository.save(purchaseOrder);

        return toPurchaseOrderResponse(purchaseOrder);
    }


    public PurchaseOrderResponse updatePurchaseOrder(PurchaseOrderRequest purchaseOrderRequest, Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElseThrow(() -> new RuntimeException("Purchase Order Not Found"));

        purchaseOrder.setOrderStatus(purchaseOrderRequest.getOrderStatus());
        purchaseOrder.setOrderDate(purchaseOrderRequest.getOrderDate());

        User user = userRepository.findById(purchaseOrderRequest.getUser_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        purchaseOrder.setUser(user);

        Supplier supplier = supplierRepository.findById(purchaseOrderRequest.getSupplier_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        purchaseOrder.setSupplier(supplier);

        purchaseOrder.getPurchaseOrderDetails().clear();

        List<PurchaseOrderDetail> purchaseOrderDetails = toPurchaseOrderDetails(purchaseOrderRequest, purchaseOrder);
        purchaseOrder.getPurchaseOrderDetails().addAll(purchaseOrderDetails);

        purchaseOrderRepository.save(purchaseOrder);

        return toPurchaseOrderResponse(purchaseOrder);
    }

    public String deletePurchaseOrder(Long purchaseOrderId) {
        if(!purchaseOrderRepository.existsById(purchaseOrderId)) {
            throw new RuntimeException("Purchase Order Not Found");
        }
        purchaseOrderRepository.deleteById(purchaseOrderId);
        return "Purchase Order Deleted Successfully";
    }

    public PurchaseOrderResponse getPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder  purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElseThrow(() -> new RuntimeException("Purchase Order Not Found"));

        return toPurchaseOrderResponse(purchaseOrder);
    }

    public List<PurchaseOrderResponse> getPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::toPurchaseOrderResponseWithoutDetails)
                .collect(Collectors.toList());
    }

    private PurchaseOrderResponse toPurchaseOrderResponseWithoutDetails(PurchaseOrder purchaseOrder) {
        return PurchaseOrderResponse.builder()
                .id(purchaseOrder.getId())
                .orderDate(purchaseOrder.getOrderDate())
                .orderStatus(purchaseOrder.getOrderStatus())
                .supplier_name(purchaseOrder.getSupplier().getName())
                .username(purchaseOrder.getUser().getFullname())
                .build();
    }

    private List<PurchaseOrderResponse.PurchaseOrderDetailResponse> toPurchaseOrderDetailResponses(PurchaseOrder purchaseOrder) {
        return purchaseOrder.getPurchaseOrderDetails().stream()
                .collect(Collectors.groupingBy(detail -> detail.getProductVariant().getProduct()))
                .entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    List<PurchaseOrderDetail> variants = entry.getValue();

                    return PurchaseOrderResponse.PurchaseOrderDetailResponse.builder()
                            .id(variants.get(0).getId())
                            .productName(product.getName())
                            .variants(variants.stream()
                                    .map(detail -> PurchaseOrderResponse.PurchaseOrderDetailResponse.Variant.builder()
                                            .id(detail.getProductVariant().getId())
                                            .quantity(detail.getQuantity())
                                            .price(detail.getPrice())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                }).collect(Collectors.toList());
    }

    private PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrder purchaseOrder) {
        return PurchaseOrderResponse.builder()
                .id(purchaseOrder.getId())
                .orderDate(purchaseOrder.getOrderDate())
                .orderStatus(purchaseOrder.getOrderStatus())
                .supplier_name(purchaseOrder.getSupplier().getName())
                .username(purchaseOrder.getUser().getFullname())
                .purchaseOrderDetails(toPurchaseOrderDetailResponses(purchaseOrder))
                .build();
    }

    private List<PurchaseOrderDetail> toPurchaseOrderDetails(PurchaseOrderRequest request, PurchaseOrder purchaseOrder) {
        return request.getPurchaseOrderDetails().stream()
                .map(detailRequest -> {
                    ProductVariant variant = productVariantRepository.findById(detailRequest.getProductVariant_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                    return PurchaseOrderDetail.builder()
                            .quantity(detailRequest.getQuantity())
                            .price(detailRequest.getPrice())
                            .productVariant(variant)
                            .purchaseOrder(purchaseOrder)
                            .build();
                }).collect(Collectors.toList());
    }

    private PurchaseOrder toPurchaseOrder(PurchaseOrderRequest request, User user, Supplier supplier) {
        return PurchaseOrder.builder()
                .orderDate(request.getOrderDate())
                .orderStatus(request.getOrderStatus())
                .supplier(supplier)
                .user(user)
                .build();
    }


}
