package org.ptithcm2021.fashionshop.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.ProductRequest;
import org.ptithcm2021.fashionshop.dto.request.ProductUpdateRequest;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductResponse;
import org.ptithcm2021.fashionshop.enums.ProductStatusEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.ProductMapper;
import org.ptithcm2021.fashionshop.model.Brand;
import org.ptithcm2021.fashionshop.model.Category;
import org.ptithcm2021.fashionshop.model.Product;
import org.ptithcm2021.fashionshop.repository.BrandRepository;
import org.ptithcm2021.fashionshop.repository.CategoryRepository;
import org.ptithcm2021.fashionshop.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

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

    @Test
    void getProductById_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> productService.getProductById(999));
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void addProduct_shouldCreateNewProduct_whenValidRequest() {
        ProductRequest request = new ProductRequest();
        request.setBrand_id(1);
        request.setCategory_id(2);
        request.setName("Sneakers");
        request.setPrice("150.0");
        request.setStock_quantity(10);
        request.setProductVariantList(Collections.singletonList(new ProductVariantRequest()));

        when(brandRepository.findById(1)).thenReturn(Optional.of(new Brand()));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any())).thenReturn(new Product());
        when(productMapper.toProductResponse(any())).thenReturn(new ProductResponse());

        ProductResponse response = productService.addProduct(request);
        assertNotNull(response);
    }

    @Test
    void updateProduct_shouldThrow_whenProductNotFound() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> productService.updateProduct(request, 1));
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void getAllProducts_shouldReturnList() {
        when(productRepository.findAll()).thenReturn(List.of(new Product()));
        when(productMapper.toProductResponse(any())).thenReturn(new ProductResponse());

        List<ProductResponse> responses = productService.getAllProducts();
        assertFalse(responses.isEmpty());
    }

    @Test
    void deleteProduct_shouldMarkAsDiscontinued_whenProductExists() {
        Product product = new Product();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1);
        assertEquals("Product deleted", result);
        assertEquals(ProductStatusEnum.DISCONTINUED, product.getStatus());
    }

    @Test
    void deleteProduct_shouldThrow_whenNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> productService.deleteProduct(1));
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, ex.getErrorCode());
    }

}
