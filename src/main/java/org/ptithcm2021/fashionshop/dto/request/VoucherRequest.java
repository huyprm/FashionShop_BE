package org.ptithcm2021.fashionshop.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequest {
    @NotBlank(message = "Code cannot be empty")
    private String code;

    @NotNull(message = "Discount type cannot be empty")
    private DiscountTypeEnum discountType;

    @NotNull(message = "Discount value cannot be empty")
    private double discountValue;

    private double minOrderValue = 0;
    private double maxDiscountValue;

    @NotNull(message = "Discount quantity cannot be left blank")
    private int quantity;

    private Date endDate;
}
