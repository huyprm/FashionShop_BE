package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.PurchaseOrderRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.PurchaseOrderResponse;
import org.ptithcm2021.fashionshop.service.PurchaseOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update/{id}")
    public ApiResponse<PurchaseOrderResponse> updatePurchaseOrder(@RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest, @PathVariable long id){
        return ApiResponse.<PurchaseOrderResponse>builder()
                .data(purchaseOrderService.updatePurchaseOrder(purchaseOrderRequest, id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<PurchaseOrderResponse> deletePurchaseOrder(@PathVariable long id){
        return ApiResponse.<PurchaseOrderResponse>builder().message(purchaseOrderService.deletePurchaseOrder(id)).build();

    }

    @GetMapping("/{id}")
    public ApiResponse<PurchaseOrderResponse> getPurchaseOrder(@PathVariable long id){
        return ApiResponse.<PurchaseOrderResponse>builder().data(purchaseOrderService.getPurchaseOrder(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<PurchaseOrderResponse>> getPurchaseOrders(){
        return ApiResponse.<List<PurchaseOrderResponse>>builder().data(purchaseOrderService.getPurchaseOrders()).build();
    }
}
