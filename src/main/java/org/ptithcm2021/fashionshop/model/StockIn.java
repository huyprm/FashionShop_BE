package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "stockIn")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockIn {
    @Id @GeneratedValue
    private long id;
    private int totalQuantity;
    private Date stockDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "purchaseOrder_id")
    private PurchaseOrder purchaseOrder;
}
