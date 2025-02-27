package org.ptithcm2021.fashionshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "carts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int quantity;
    private double totalPrice;

    @ManyToOne
    @JoinColumn (name = "productVariant_id")
    private ProductVariant productVariant;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartDiscountDetail> cartDiscountDetails;
}
