package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.PurchaseOrderRequest;
import org.ptithcm2021.fashionshop.dto.response.PurchaseOrderResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.PurchaseOrderMapper;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.ProductRepository;
import org.ptithcm2021.fashionshop.repository.PurchaseOrderRepository;
import org.ptithcm2021.fashionshop.repository.SupplierRepository;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) {
        User user = userRepository.findById(purchaseOrderRequest.getUser_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Supplier supplier = supplierRepository.findById(purchaseOrderRequest.getSupplier_id()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setOrderStatus(purchaseOrderRequest.getOrderStatus());
        purchaseOrder.setOrderDate(purchaseOrderRequest.getOrderDate());
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setUser(user);

        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderRequest.getPurchaseOrderDetails().stream().map(purchaseOrderDetailRequest -> {
           PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();

           purchaseOrderDetail.setQuantity(purchaseOrderDetailRequest.getQuantity());
           purchaseOrderDetail.setPrice(purchaseOrderDetailRequest.getPrice());

           Product product = productRepository.findById(purchaseOrderDetailRequest.getProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
           purchaseOrderDetail.setProduct(product);
           purchaseOrderDetail.setPurchaseOrder(purchaseOrder);

           return purchaseOrderDetail;
        }).collect(Collectors.toList());

        purchaseOrder.setPurchaseOrderDetails(purchaseOrderDetails);

        purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrderMapper.toResponse(purchaseOrder);
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

        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderRequest.getPurchaseOrderDetails().stream().map(purchaseOrderDetailRequest -> {
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();

            purchaseOrderDetail.setQuantity(purchaseOrderDetailRequest.getQuantity());
            purchaseOrderDetail.setPrice(purchaseOrderDetailRequest.getPrice());

            Product product = productRepository.findById(purchaseOrderDetailRequest.getProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            purchaseOrderDetail.setProduct(product);
            purchaseOrderDetail.setPurchaseOrder(purchaseOrder);

            return purchaseOrderDetail;
        }).collect(Collectors.toList());

        purchaseOrder.getPurchaseOrderDetails().addAll(purchaseOrderDetails);

        purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrderMapper.toResponse(purchaseOrder);
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

        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    public List<PurchaseOrderResponse> getPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        return purchaseOrders.stream().map(purchaseOrder -> {

            PurchaseOrderResponse purchaseOrderResponse = purchaseOrderMapper.toResponse(purchaseOrder);

            purchaseOrderResponse.setPurchaseOrderDetails(null);

            return purchaseOrderResponse;
        }).collect(Collectors.toList());
    }
}
