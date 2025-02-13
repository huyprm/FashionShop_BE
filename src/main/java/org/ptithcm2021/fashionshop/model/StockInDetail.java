package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "stockInDetails")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "stockIn_id")
    private StockIn stockIn;
}
