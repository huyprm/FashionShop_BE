package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.OrderRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.OrderResponse;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.model.Order;
import org.ptithcm2021.fashionshop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest, @RequestParam String userId) {

        return ApiResponse.<OrderResponse>builder().data(orderService.createOrder(orderRequest, userId)).build();
    }

    @GetMapping("/{userId}/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable long orderId, @PathVariable String userId){
        return ApiResponse.<OrderResponse>builder().data(orderService.getOrder(orderId, userId)).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<OrderResponse>> getAllOrderByStatus(@PathVariable String userId, @RequestParam OrderStatusEnum status){
        return ApiResponse.<List<OrderResponse>>builder().data(orderService.getOrderByStatus(userId, status)).build();
    }

    @DeleteMapping("/{userId}/{orderId}")
    public ApiResponse<String> cancelOrder(@PathVariable long orderId, @PathVariable String userId){
        orderService.cancelOrder(orderId, userId);
        return ApiResponse.<String>builder().message("Cancel order successfully").build();
    }

}

