package org.ptithcm2021.fashionshop.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.CategoryRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.CategoryResponse;
import org.ptithcm2021.fashionshop.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable int id) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryById(id)).build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(categoryRequest))
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable int id, @RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategory(id, categoryRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .data(categoryService.deleteCategory(id))
                .build();
    }
}
