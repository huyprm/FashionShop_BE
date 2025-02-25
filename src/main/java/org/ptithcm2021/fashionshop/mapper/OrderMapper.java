package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.fashionshop.dto.request.OrderRequest;
import org.ptithcm2021.fashionshop.dto.response.OrderResponse;
import org.ptithcm2021.fashionshop.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    Order toOrder(OrderRequest orderRequest);
}
