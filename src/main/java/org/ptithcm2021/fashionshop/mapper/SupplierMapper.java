package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.fashionshop.dto.request.SupplierRequest;
import org.ptithcm2021.fashionshop.dto.response.SupplierResponse;
import org.ptithcm2021.fashionshop.model.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierResponse toSupplierResponse(Supplier supplier);
    Supplier toSupplier(SupplierRequest supplierRequest);
    void toUpdateSupplier(@MappingTarget Supplier supplier, SupplierRequest supplierRequest);
}
