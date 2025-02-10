package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantRequest {
    @Min(value = 1, message = "Quantity must be greater than or equal to 1.")
    private int quantity;
    @Min(value = 0, message = "Product price cannot be less than 0.")
    private double price;
    private String image;
    private String color;
    private String size;
}
