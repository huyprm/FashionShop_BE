package org.ptithcm2021.fashionshop.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.BrandRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.BrandResponse;
import org.ptithcm2021.fashionshop.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/create")
    public ApiResponse<BrandResponse> createBrand(@RequestBody BrandRequest brand) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.crateBrand(brand))
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<BrandResponse> updateBrand(@RequestBody BrandRequest brand,@PathVariable int id) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.updateBrand(brand, id))
                .build();
    }

    @DeleteMapping("{id}/delete")
    public ApiResponse<String> deleteBrand(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message(brandService.deleteBrand(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder()
                .data(brandService.getAllBrands()).build();
    }

    @GetMapping("{id}")
    public ApiResponse<BrandResponse> getBrand(@PathVariable int id) {
        return ApiResponse.<BrandResponse>builder()
                .data(brandService.getBrandById(id)).build();
    }


}
