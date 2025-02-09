package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_variants")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product_variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;
    private int quantity;
    private double price;
    private String image;
    private String color;
    private String size;

}
