package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.ptithcm2021.fashionshop.dto.request.VoucherRequest;
import org.ptithcm2021.fashionshop.dto.response.VoucherResponse;
import org.ptithcm2021.fashionshop.model.Voucher;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    VoucherResponse toVoucherResponse(Voucher voucher);
    Voucher toVoucher(VoucherRequest voucherRequest);

    void updateVoucher(@MappingTarget Voucher voucher, VoucherRequest voucherRequest);
}
