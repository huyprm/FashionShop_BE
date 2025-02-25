package org.ptithcm2021.fashionshop.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;
import org.ptithcm2021.fashionshop.model.Product;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BundleDiscountRequest {
    @NotNull(message = "product discount cannot be null")
    private int mainProduct_id;

    @NotNull(message = "product discount cannot be null")
    private int bundledProduct_id;

    @Enumerated(EnumType.STRING)
    private DiscountTypeEnum discountType = DiscountTypeEnum.PERCENTAGE;

    @NotNull(message = "Discount value cannot null")
    private double discountValue;


    @NotNull(message = "start time cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "end time cannot be null")
    private LocalDateTime endDate;
}
