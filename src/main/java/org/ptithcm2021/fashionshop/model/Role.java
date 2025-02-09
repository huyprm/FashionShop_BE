package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.RoleEnum;

import java.util.List;

@Entity(name ="roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    private String role;
    @Column(nullable = false)
    private String description;
    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private List<User> users;
}
