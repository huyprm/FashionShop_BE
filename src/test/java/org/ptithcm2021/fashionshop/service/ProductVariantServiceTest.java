package org.ptithcm2021.fashionshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductVariantResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.ProductVariantMapper;
import org.ptithcm2021.fashionshop.model.Product;
import org.ptithcm2021.fashionshop.model.ProductVariant;
import org.ptithcm2021.fashionshop.repository.ProductRepository;
import org.ptithcm2021.fashionshop.repository.ProductVariantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductVariantServiceTest {

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private ProductVariantMapper productVariantMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductVariantService productVariantService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateProductVariants_Success() {
        int productId = 1;
        Product product = new Product();

        ProductVariantRequest request = new ProductVariantRequest();
        ProductVariant variant = new ProductVariant();
        ProductVariantResponse response = new ProductVariantResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productVariantMapper.toProductVariant(request)).thenReturn(variant);
        when(productVariantMapper.toProductVariantResponseList(anyList())).thenReturn(List.of(response));

        List<ProductVariantResponse> result = productVariantService.createProductVariants(List.of(request), productId);

        assertEquals(1, result.size());
        verify(productVariantRepository).saveAll(anyList());
    }

    @Test
    void testCreateProductVariants_ProductNotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () ->
                productVariantService.createProductVariants(List.of(new ProductVariantRequest()), 1));

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testUpdateProductVariant_Success() {
        int variantId = 1;
        ProductVariant variant = new ProductVariant();
        ProductVariantRequest request = new ProductVariantRequest();
        ProductVariantResponse response = new ProductVariantResponse();

        when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
        when(productVariantRepository.saveAll(anyList())).thenReturn(List.of(variant));
        when(productVariantMapper.toProductVariantResponseList(anyList())).thenReturn(List.of(response));

        Map<Integer, ProductVariantRequest> map = Map.of(variantId, request);
        List<ProductVariantResponse> result = productVariantService.updateProductVariant(map);

        assertEquals(1, result.size());
        verify(productVariantMapper).updateProductVariant(variant, request);
    }

    @Test
    void testUpdateProductVariant_ProductNotFound() {
        when(productVariantRepository.findById(anyInt())).thenReturn(Optional.empty());

        Map<Integer, ProductVariantRequest> map = Map.of(1, new ProductVariantRequest());

        AppException exception = assertThrows(AppException.class, () ->
                productVariantService.updateProductVariant(map));

        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testDeleteProductVariant_Success() {
        List<Integer> ids = Arrays.asList(1, 2, 2, 3);

        String result = productVariantService.deleteProductVariant(ids);

        assertEquals("List product variant deleted", result);
        verify(productVariantRepository).deleteAllById(List.of(1, 2, 3));
    }
}
