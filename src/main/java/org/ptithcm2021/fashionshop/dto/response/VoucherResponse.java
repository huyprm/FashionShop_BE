package org.ptithcm2021.fashionshop.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherResponse {
    private int id;
    private String code;
    private DiscountTypeEnum discountType;
    private double discountValue;
    private double minOrderValue;
    private double maxDiscountValue;
    private int quantity;
    private Date endDate;
}
