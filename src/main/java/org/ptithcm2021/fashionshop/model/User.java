package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.fashionshop.enums.UserStatusEnum;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String phone;
    @Size(min = 5, message = "Password must be greater than 5 characters")
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @Column(unique = true)
    @Email(message = "Email is not in correct format")
    private String email;
    private String fullname;
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status = UserStatusEnum.PENDING;
    @Column(columnDefinition = "text")
    private String refreshToken;
    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
