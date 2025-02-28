package org.ptithcm2021.fashionshop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;
import org.ptithcm2021.fashionshop.enums.PaymentStatusEnum;
import org.ptithcm2021.fashionshop.model.OrderDetail;
import org.ptithcm2021.fashionshop.model.ProductVariant;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long id;
    private OrderStatusEnum status;
    private LocalDateTime date;
    private double total_price;
    private double voucherDiscount_price;
    private PaymentMethodEnum paymentMethod;
    private PaymentStatusEnum paymentStatus;
    private List<OrderDetailResponse> orderDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetailResponse {
        private int quantity;
        private double price;

        @JsonIgnoreProperties("product")
        private ProductVariant productVariant;
    }
}
