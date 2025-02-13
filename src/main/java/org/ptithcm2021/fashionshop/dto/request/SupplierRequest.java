package org.ptithcm2021.fashionshop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {
    @NotBlank(message = "Supplier name cannot be left blank.")
    private String name;

    private String address;
    private String phone;

    @Email(message = "Email is not in correct format.")
    private String email;
}
