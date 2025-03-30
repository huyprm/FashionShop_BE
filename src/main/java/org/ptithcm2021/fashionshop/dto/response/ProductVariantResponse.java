package org.ptithcm2021.fashionshop.dto.response;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantResponse {
    private int id;
    private int quantity;
    private double price;
    private String image;
    private String color;
    private String size;
    private int productId;
}
