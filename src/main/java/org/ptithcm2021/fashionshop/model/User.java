package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;

import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String phone;

    @Size(min = 5, message = "Password must be greater than 5 characters")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(unique = true)
    @Email(message = "Email is not in correct format")
    private String email;

    private String fullname;
    private String address;
    private String birthday;
    private String gender;
    private String img;

    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status = AccountStatusEnum.PENDING;

    @Column(columnDefinition = "text")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<PurchaseOrder> purchaseOrder;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;


}
