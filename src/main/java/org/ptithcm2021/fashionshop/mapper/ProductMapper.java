package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.fashionshop.dto.request.ProductRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductResponse;
import org.ptithcm2021.fashionshop.model.Product;

@Mapper(componentModel = "spring", uses = {BundleDiscountMapper.class})

public interface ProductMapper {
    Product toProduct(ProductRequest product);

    //@Mapping(target = "bundleDiscountList", source = "bundleDiscountList")
    ProductResponse toProductResponse(Product product);
}
