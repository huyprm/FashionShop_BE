package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @Email(message = "Email is not in correct format")
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Size(min = 5, message = "Password must be greater than 5 characters")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
