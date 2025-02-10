package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductVariantResponse;
import org.ptithcm2021.fashionshop.model.Product_variant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    Product_variant toProductVariant(ProductVariantRequest product_variant);
    ProductVariantResponse toProductVariantResponse(Product_variant product_variant);
    List<ProductVariantResponse> toProductVariantResponseList(List<Product_variant> product_variants);
    void updateProductVariant(@MappingTarget Product_variant product_variant, ProductVariantRequest product_variant_request);
}
