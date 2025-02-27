package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.request.CartUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    public final CartService cartService;

    @PostMapping("/create")
    public ApiResponse<CartResponse> createCart(@RequestBody @Valid CartRequest cartRequest) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.createCart(cartRequest)).build();
    }

    @PutMapping("/update")
    public ApiResponse<CartResponse> updateCart(@RequestBody @Valid CartUpdateRequest cartRequest) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.updateCart(cartRequest)).build();
    }

    @GetMapping()
    public ApiResponse<List<CartResponse>> getCart(@RequestParam String userId) {
        return ApiResponse.<List<CartResponse>>builder()
                .data(cartService.getCartByUserId(userId)).build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteCart(@RequestParam int id, @RequestParam String typeProduct) {
        return ApiResponse.<String>builder().data(cartService.deleteCart(id, typeProduct)).build();
    }
}
