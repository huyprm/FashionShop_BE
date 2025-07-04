package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    @Size(min = 100, message = "Description must be as least 100 character")
    private String description;
    private List<String> images;
    private String thumbnail;
    private String price;
    @Min(value = 1, message = "Quantity must be greater than or equal to 1.")
    private int stock_quantity;
    private int category_id;
    private int brand_id;
    private List<ProductVariantRequest> productVariantList;

}
