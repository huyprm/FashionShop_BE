package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.ProductStatusEnum;

import java.util.List;

@Entity
@Table(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ElementCollection
    private List<String> images;

    private String price;
    private int stock_quantity;

    @Column
    private double rating = 0;

    @Column
    private int sold = 0;

    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status = ProductStatusEnum.IN_STOCK;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> productVariantList;
}
