package org.ptithcm2021.fashionshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductVariantResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.ProductVariantMapper;
import org.ptithcm2021.fashionshop.model.Product;
import org.ptithcm2021.fashionshop.model.Product_variant;
import org.ptithcm2021.fashionshop.repository.ProductRepository;
import org.ptithcm2021.fashionshop.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final ProductRepository productRepository;

    public List<ProductVariantResponse> createProductVariants(List<ProductVariantRequest> productVariantRequests, int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Product_variant> list = productVariantRequests.stream()
                .map(productVariantRequest -> {
                    Product_variant productVariant = productVariantMapper.toProductVariant(productVariantRequest);
                    productVariant.setProduct(product);
                    return productVariant;
                }).toList();

        productVariantRepository.saveAll(list);

        return productVariantMapper.toProductVariantResponseList(list);
    }

    @Transactional
    public List<ProductVariantResponse> updateProductVariant(Map<Integer, ProductVariantRequest> productVariantRequests) {
        List<Product_variant> list = productVariantRequests.entrySet().stream()
                .map(productVariantRequest ->{
                    Product_variant productVariant = productVariantRepository.findById(productVariantRequest.getKey()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                    productVariantMapper.updateProductVariant(productVariant, productVariantRequest.getValue());
                    return productVariant;
                }).toList();

        return productVariantMapper.toProductVariantResponseList(productVariantRepository.saveAll(list));
    }

    public String deleteProductVariant(List<Integer> ids) {
        List<Integer> list = ids.stream().distinct().collect(Collectors.toList());

        productVariantRepository.deleteAllById(list);
        return "List product variant deleted";
    }
}
