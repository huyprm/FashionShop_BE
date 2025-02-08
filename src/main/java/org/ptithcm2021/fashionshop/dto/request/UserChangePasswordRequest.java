package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserChangePasswordRequest {
    private String oldPassword;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, message = "Password must be greater than 5 character")
    private String newPassword;
}
