package org.ptithcm2021.fashionshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.service.VNPayService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class VNPayController {
    private final VNPayService vnPayService;

    @PostMapping("/vnpay")
    public ApiResponse<String> vnpayMethod (@RequestParam int total_price, @RequestParam String orderId) {
        return ApiResponse.<String>builder().data(vnPayService.createOrder(total_price, orderId)).build();
    }


    @PostMapping("/returnStatus")
    public ApiResponse<Integer> returnStatus (HttpServletRequest request) {
        return ApiResponse.<Integer>builder().data(vnPayService.orderReturn(request)).build();
    }
}
