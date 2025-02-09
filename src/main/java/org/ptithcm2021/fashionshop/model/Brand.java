package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "brands")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @OneToMany(mappedBy = "brand",cascade = CascadeType.ALL)
    private List<Product> products;
}
