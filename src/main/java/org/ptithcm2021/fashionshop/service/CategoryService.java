package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.CategoryRequest;
import org.ptithcm2021.fashionshop.dto.response.CategoryResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.model.Category;
import org.ptithcm2021.fashionshop.repository.CategoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final CategoryRepository categoryRepository;

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.save(Category.builder().name(categoryRequest.getName()).build());

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponse> categoriesResponse = new ArrayList<>();

        for (Category category : categories) {
            categoriesResponse.add(CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build());
        }

        return categoriesResponse;
    }

    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return CategoryResponse.builder()
                .name(category.getName())
                .id(category.getId())
                .build();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")    public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(categoryRequest.getName());

        categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")    public String deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if(!category.getProducts().isEmpty()) throw new AppException(ErrorCode.FORBIDDEN);

        categoryRepository.deleteById(id);

        return "Category deleted";
    }
}
