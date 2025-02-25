package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.fashionshop.dto.request.BDiscountUpdateRequest;
import org.ptithcm2021.fashionshop.dto.request.BundleDiscountRequest;
import org.ptithcm2021.fashionshop.dto.response.BundleDiscountResponse;
import org.ptithcm2021.fashionshop.model.BundleDiscount;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BundleDiscountMapper {
    BundleDiscount toBundleDiscount(BundleDiscountRequest discount);

    @Mapping(target = "bundledProduct_name", source = "bundledProduct.name")
    @Mapping(target = "bundledProduct_thumbnail", source = "bundledProduct.thumbnail")
    //@Mapping(target = "productVariantResponseList", source = "bundledProduct.productVariantList")
    BundleDiscountResponse toBundleDiscountResponse(BundleDiscount discount);

    void toUpdateBundleDiscount(@MappingTarget BundleDiscount discount, BDiscountUpdateRequest discountRequest);

    List<BundleDiscountResponse> toBundleDiscountResponseList(List<BundleDiscount> discounts);
}