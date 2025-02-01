package org.ptithcm2021.fashionshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String phone;
    @Min(value = 5)
    @NotBlank(message = "Password cannot be blank")
    private String password;
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
