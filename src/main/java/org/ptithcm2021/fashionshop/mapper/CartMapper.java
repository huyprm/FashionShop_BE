package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ptithcm2021.fashionshop.dto.request.BundleDiscountRequest;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.response.BundleDiscountResponse;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.model.BundleDiscount;
import org.ptithcm2021.fashionshop.model.Cart;
import org.ptithcm2021.fashionshop.model.CartDiscountDetail;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BundleDiscountMapper.class})
public interface CartMapper {
    @Mapping(target = "cartDiscountDetails", source = "cartDiscountDetails")
    Cart toCart(CartRequest cartRequest);
    @Mapping(target = "cartDiscountDetails", qualifiedByName = "mapBundle")
    CartResponse toCartResponse(Cart cart);

    @Named("mapBundle")
    List<CartResponse.DiscountDetailResponse> toDiscountDetailResponseList(List<CartDiscountDetail> CartDiscountDetails);
}
