package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.RoleEnum;

@Entity(name ="role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;
    @Column(nullable = false)
    private String description;

}
