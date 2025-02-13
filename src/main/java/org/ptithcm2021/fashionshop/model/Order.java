package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.PENDING;

    private LocalDateTime date;

    private String address;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double voucherDiscount;

    private double bundleDiscount;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
