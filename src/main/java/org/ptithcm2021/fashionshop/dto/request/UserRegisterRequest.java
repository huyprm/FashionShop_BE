package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String fullname;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, message = "Password must be greater than 5 character")
    private String password;
    @Email(message = "Email is not in correct format")
    @NotBlank(message = "Email is not blank")
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String birthday;
}
