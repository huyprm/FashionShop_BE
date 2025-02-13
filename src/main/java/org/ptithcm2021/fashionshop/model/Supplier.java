package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;

@Entity(name = "suppliers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    private String address;
    private String phone;

    @Email(message = "Email is not in correct format.")
    private String email;

    @Enumerated(EnumType.STRING)
    //@Builder.Default
    private AccountStatusEnum status = AccountStatusEnum.ACTIVE;
}
