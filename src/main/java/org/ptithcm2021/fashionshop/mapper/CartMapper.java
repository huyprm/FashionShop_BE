package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "cartDiscountDetails", source = "cartDiscountDetails")
    Cart toCart(CartRequest cartRequest);

    CartResponse toCartResponse(Cart cart);
}
