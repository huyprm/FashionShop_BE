package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "cartDetails")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDiscountDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn (name = "productVariant_id")
    private ProductVariant productVariant;

    @OneToOne
    @JoinColumn(name = "bundleDiscount_id")
    private BundleDiscount bundleDiscount;

    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;
}
