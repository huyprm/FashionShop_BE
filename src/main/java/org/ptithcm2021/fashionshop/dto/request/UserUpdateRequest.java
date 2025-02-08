package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {
    private String fullname;
    @Email(message = "Email is not in correct format")
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String birthday;
}
