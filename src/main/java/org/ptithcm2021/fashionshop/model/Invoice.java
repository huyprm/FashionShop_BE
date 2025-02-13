package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity(name = "invoices")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;


    private Instant issueDate = Instant.now();

    private double totalAmount;



}
