package org.ptithcm2021.fashionshop.controller;


import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.response.ProductResponse;
import org.ptithcm2021.fashionshop.mapper.ProductMapper;
import org.ptithcm2021.fashionshop.model.Brand;
import org.ptithcm2021.fashionshop.model.Category;
import org.ptithcm2021.fashionshop.model.Product;
import org.ptithcm2021.fashionshop.repository.ProductRepository;
import org.ptithcm2021.fashionshop.service.ProductService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;
    private ProductResponse mockResponse;
    @BeforeEach
    void setUp() {
        Brand mockBrand = Brand.builder().id(1).name("đfdffdf").build();
        Category mockCategory = Category.builder().id(1).name("dfdfdf").build();
        mockProduct = Product.builder()
                .id(1)
                .name("Laptop")
                .brand(mockBrand)
                .category(mockCategory)
                .build();
        mockResponse = ProductResponse.builder()
                .id(1)
                .name("Laptop")
                .price("1500.0")
                .build();
    }


    @Test
    public void test() {
        // Khi gọi findById() thì trả về mockProduct
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        // Khi gọi productMapper.toProductResponse() thì trả về mockResponse
        when(productMapper.toProductResponse(mockProduct)).thenReturn(mockResponse);

        // Gọi service method
        ProductResponse result = productService.getProductById(1);

        // Kiểm tra kết quả
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("1500.0", result.getPrice());
        // Kiểm tra các method đã được gọi đúng số lần
        verify(productRepository, times(1)).findById(1);
    }

}
