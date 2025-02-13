package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.VoucherRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.VoucherResponse;
import org.ptithcm2021.fashionshop.service.VoucherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("/{id}")
    public ApiResponse<VoucherResponse> getVoucherById(@PathVariable("id") int id) {

        return ApiResponse.<VoucherResponse>builder().data(voucherService.getVoucherById(id)).build();
    }

    @GetMapping
    public ApiResponse<List<VoucherResponse>> getAllVoucher() {

        return ApiResponse.<List<VoucherResponse>>builder().data(voucherService.getAllVouchers()).build();
    }

    @PostMapping("/create")
    public ApiResponse<VoucherResponse> createVoucher(@RequestBody @Valid VoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder().data(voucherService.createVoucher(request)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<VoucherResponse> updateVoucher(@PathVariable("id") int id, @RequestBody @Valid VoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder().data(voucherService.updateVoucher(id, request)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteVoucher(@PathVariable int id) {
        return ApiResponse.<String>builder().message(voucherService.deleteVoucher(id)).build();
    }
}
