package org.ptithcm2021.fashionshop.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.BDiscountUpdateRequest;
import org.ptithcm2021.fashionshop.dto.request.BundleDiscountRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.BundleDiscountResponse;
import org.ptithcm2021.fashionshop.service.BundleDiscountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bundle_discounts")
public class  BundleDiscountController {
    private final BundleDiscountService bundleDiscountService;

    @PostMapping("/create")
    public ApiResponse<BundleDiscountResponse> createBundleDiscount(@RequestBody BundleDiscountRequest bundleDiscountRequest) {
        return ApiResponse.<BundleDiscountResponse>builder().
                data(bundleDiscountService.createBundleDiscount(bundleDiscountRequest)).build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<BundleDiscountResponse> updateBundleDiscount(@RequestBody BDiscountUpdateRequest request,
                                                                    @PathVariable int id) {
        return ApiResponse.<BundleDiscountResponse>builder()
                .data(bundleDiscountService.updateBundleDiscount(request, id)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BundleDiscountResponse> getBundleDiscount(@PathVariable int id) {
        return ApiResponse.<BundleDiscountResponse>builder()
                .data(bundleDiscountService.getBundleDiscount(id)).build();
    }

    @GetMapping
    public ApiResponse<List<BundleDiscountResponse>> getBundleDiscounts() {
        return ApiResponse.<List<BundleDiscountResponse>>builder()
                .data(bundleDiscountService.getAllBundleDiscount()).build();
    }
}
