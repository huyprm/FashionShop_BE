package org.ptithcm2021.fashionshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;

import java.time.LocalDateTime;

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
}
