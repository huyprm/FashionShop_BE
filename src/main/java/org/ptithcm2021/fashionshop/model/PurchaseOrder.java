package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.query.Order;
import org.ptithcm2021.fashionshop.enums.OrderStatusEnum;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity(name = "purchaseOrders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus = OrderStatusEnum.PENDING;

    @ManyToOne()
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderDetail> purchaseOrderDetails;
}
