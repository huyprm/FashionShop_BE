package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.fashionshop.dto.request.ProductRequest;
import org.ptithcm2021.fashionshop.dto.response.ProductResponse;
import org.ptithcm2021.fashionshop.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest product);
    ProductResponse toProductResponse(Product product);
}
