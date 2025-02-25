package org.ptithcm2021.fashionshop.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BDiscountUpdateRequest {
    @Enumerated(EnumType.STRING)
    private DiscountTypeEnum discountType = DiscountTypeEnum.PERCENTAGE;

    @NotNull(message = "Discount value cannot null")
    private double discountValue;

//    @Min(value = 1, message = "Minimum value is 1")
//    private int maxQuantity = 1;

    @NotNull(message = "start time cannot be null")
    private Date startDate;

    @NotNull(message = "start time cannot be null")
    private Date endDate;
}
