package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.PaymentMethodEnum;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotNull
    private List<Integer> cartId;

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    private String voucherCode;

    @NotNull
    private PaymentMethodEnum paymentMethod;
}
