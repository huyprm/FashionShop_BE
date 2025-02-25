package org.ptithcm2021.fashionshop.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;
import org.ptithcm2021.fashionshop.enums.PaymentStatusEnum;
import org.ptithcm2021.fashionshop.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    @NotBlank
    private String user_id;

    private double voucherDiscount_price;

    @NotNull
    private List<OrderDetailRequest> orderDetails;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod = PaymentMethodEnum.COD;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailRequest {
        @NotNull
        private int quantity;

        @NotNull
        private int productVariant_id;
    }
}
