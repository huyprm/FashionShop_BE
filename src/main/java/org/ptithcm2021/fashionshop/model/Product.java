package org.ptithcm2021.fashionshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.ProductStatusEnum;

import java.util.List;

@Entity(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ElementCollection
    private List<String> images;

    private String thumbnail;

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

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> productVariantList;

    @OneToMany(mappedBy = "mainProduct")
    private List<BundleDiscount> bundleDiscountList;
}
