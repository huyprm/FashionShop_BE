package org.ptithcm2021.fashionshop.mapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Helper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ptithcm2021.fashionshop.dto.request.PurchaseOrderRequest;
import org.ptithcm2021.fashionshop.dto.response.PurchaseOrderResponse;
import org.ptithcm2021.fashionshop.model.PurchaseOrder;
import org.ptithcm2021.fashionshop.model.PurchaseOrderDetail;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {


//    @Mapping(target = "supplier", expression = "java(helper.findSupplierById(purchaseOrderRequest.getSupplier_id()))")
//    @Mapping(target = "user", expression = "java(helper.findUserById(purchaseOrderRequest.getUser_id()))")
//    @Mapping(target ="purchaseOrderDetail", source = "purchaseOrderDetail")
//    PurchaseOrder toPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest);

    @Mapping(target = "supplier_name", source = "supplier.name")
    @Mapping(target = "username", source = "user.fullname")
    PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrder purchaseOrder);

//    @Mapping(target = "product", expression = "java(helper.findProductById(purchaseOrderDetailRequest.getProduct_id()))")
//    PurchaseOrderDetail toPurchaseOrderDetail(PurchaseOrderRequest.PurchaseOrderDetailRequest purchaseOrderDetailRequest);
}
