package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;

import java.util.Date;

@Entity(name = "bundle_discounts")
@AllArgsConstructor
@NoArgsConstructor
public class BundleDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "main_product_id")
    private Product mainProduct_id;

    @ManyToOne
    @JoinColumn(name = "bundled_product_id")
    private Product bundledProduct;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountTypeEnum discountType;

    @Column(nullable = false)
    private double discountValue;

    @Column
    private int maxQuantity = 1;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

}
