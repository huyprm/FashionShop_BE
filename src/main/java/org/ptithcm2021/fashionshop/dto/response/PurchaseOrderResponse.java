package org.ptithcm2021.fashionshop.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.PurchaseOrderRequest;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;
import org.ptithcm2021.fashionshop.model.PurchaseOrderDetail;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderResponse {
    private int id;
    private Date orderDate;
    private OrderStatusEnum orderStatus;
    private String supplier_name;
    private String username;
    private List<PurchaseOrderDetailResponse> purchaseOrderDetail;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PurchaseOrderDetailResponse {
        private int id;
        private String productName;
        private int quantity;
        private double price;
    }
}
