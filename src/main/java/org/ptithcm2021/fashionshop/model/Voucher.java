package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;

import java.util.Date;

@Entity(name = "vouchers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountTypeEnum discountType;

    @Column(nullable = false)
    private double discountValue = 0;

    @Column
    private double minOrderValue = 0;

    private double maxDiscountValue;

    @Column(nullable = false)
    private int quantity;

    private Date endDate;
}
