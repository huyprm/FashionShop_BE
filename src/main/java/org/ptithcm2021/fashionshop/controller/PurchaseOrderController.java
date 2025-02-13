package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.PurchaseOrderRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.PurchaseOrderResponse;
import org.ptithcm2021.fashionshop.service.PurchaseOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchaseOrders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/create")
    public ApiResponse<PurchaseOrderResponse> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .data(purchaseOrderService.createPurchaseOrder(purchaseOrderRequest)).build();
    }
}
