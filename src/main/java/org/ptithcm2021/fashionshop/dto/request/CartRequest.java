package org.ptithcm2021.fashionshop.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @NotNull
    private String user_id;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1.")
    private int quantity;

    @Valid
    private List<DiscountDetailRequest> cartDiscountDetails;

    @NotNull
    private int productVariant_id;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountDetailRequest {
        @NotNull
        private int productVariant_id;

        @NotNull
        private int bundleDiscount_id;

        @Min(value = 1, message = "Quantity must be greater than or equal to 1.")
        private int quantity;
    }
}
