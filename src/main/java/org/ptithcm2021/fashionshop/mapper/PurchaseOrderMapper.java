package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.*;
import org.ptithcm2021.fashionshop.dto.response.PurchaseOrderResponse;
import org.ptithcm2021.fashionshop.model.PurchaseOrder;
import org.ptithcm2021.fashionshop.model.PurchaseOrderDetail;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(source = "supplier.name", target = "supplier_name")
    @Mapping(source = "user.fullname", target = "username")
    @Mapping(source = "purchaseOrderDetails", target = "purchaseOrderDetails", qualifiedByName = "mapDetails")
    PurchaseOrderResponse toResponse(PurchaseOrder purchaseOrder);

    @Named("mapDetails")
    default List<PurchaseOrderResponse.PurchaseOrderDetailResponse> mapDetails(List<PurchaseOrderDetail> details) {
        return details.stream().map(this::toDetailResponse).collect(Collectors.toList());
    }

    @Mapping(source = "product.name", target = "productName")
    PurchaseOrderResponse.PurchaseOrderDetailResponse toDetailResponse(PurchaseOrderDetail detail);
}
