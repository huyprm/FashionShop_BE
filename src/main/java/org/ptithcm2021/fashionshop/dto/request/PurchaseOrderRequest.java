package org.ptithcm2021.fashionshop.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.model.Product;
import org.ptithcm2021.fashionshop.model.PurchaseOrder;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequest {
    @NotNull(message = "Order time cannot be left blank")
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
    @NotNull(message = "Please select a provider.")
    private int supplier_id;

    @NotNull
    private String user_id;

    @NotNull(message = "Please enter the product ordered.")
    private List<PurchaseOrderDetailRequest> purchaseOrderDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseOrderDetailRequest {
        @NotNull
        private int quantity;
        @NotNull
        private double price;
        @NotNull
        private int productVariant_id;

    }
}
