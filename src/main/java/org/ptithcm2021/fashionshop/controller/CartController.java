package org.ptithcm2021.fashionshop.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.service.CartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    public final CartService cartService;

    @PostMapping("/create")
    public ApiResponse<CartResponse> createCart(@RequestBody CartRequest cartRequest) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.createCart(cartRequest)).build();
    }
}
