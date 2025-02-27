package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateRequest {
    @NotNull
    private int cartId;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1.")
    private int quantity;

    @NotNull
    private String userId;

    private Map<Integer, Integer> productDiscount;

}
