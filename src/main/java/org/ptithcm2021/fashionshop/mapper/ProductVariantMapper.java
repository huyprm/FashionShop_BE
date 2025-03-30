package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductVariantResponse;
import org.ptithcm2021.fashionshop.model.ProductVariant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariant toProductVariant(ProductVariantRequest product_variant);
    @Mapping(target = "productId", source = "product.id")
    ProductVariantResponse toProductVariantResponse(ProductVariant product_variant);
    List<ProductVariantResponse> toProductVariantResponseList(List<ProductVariant> product_variants);
    void updateProductVariant(@MappingTarget ProductVariant product_variant, ProductVariantRequest product_variant_request);
}
