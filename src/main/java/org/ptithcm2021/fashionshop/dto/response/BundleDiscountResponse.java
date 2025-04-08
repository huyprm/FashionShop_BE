package org.ptithcm2021.fashionshop.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BundleDiscountResponse {
    private int id;
    private String bundledProduct_name;
    private String bundledProduct_thumbnail;
    private DiscountTypeEnum discountType;
    private double discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
