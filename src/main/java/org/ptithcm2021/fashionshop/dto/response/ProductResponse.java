package org.ptithcm2021.fashionshop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.enums.ProductStatusEnum;
import org.ptithcm2021.fashionshop.model.BundleDiscount;
import org.ptithcm2021.fashionshop.model.ProductVariant;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private int id;
    private String name;
    private String description;
    private List<String> images;
    private String thumbnail;
    private String price;
    private int stock_quantity;
    private CategoryResponse category;
    private BrandResponse brand;
    private int sold;
    private ProductStatusEnum status;
    @JsonIgnoreProperties("product")
    private List<ProductVariant> productVariantList;
    private List<BundleDiscountResponse> bundleDiscountList;
}
