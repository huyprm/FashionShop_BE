package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "payments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    private long id;

    private double amountPaid;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
