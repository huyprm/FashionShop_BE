package org.ptithcm2021.fashionshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "product_variants")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;
    @Min(value = 1, message = "Quantity must be greater than or equal to 1.")
    private int quantity;
    @Min(value = 0, message = "Product price cannot be less than 0.")
    private double price;
    private String image;
    private String color;
    private String size;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
