package org.ptithcm2021.fashionshop.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.model.CartDiscountDetail;
import org.ptithcm2021.fashionshop.model.ProductVariant;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private int id;
    private double totalPrice;
    private int quantity;
    private ProductVariant productVariant;

    private List<DiscountDetailResponse> cartDiscountDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DiscountDetailResponse {

        private ProductVariant productVariant;
        private double price;
        private int quantity;
    }
}
