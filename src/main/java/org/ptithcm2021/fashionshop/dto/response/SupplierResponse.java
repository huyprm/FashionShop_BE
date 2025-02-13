package org.ptithcm2021.fashionshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierResponse {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private AccountStatusEnum status;
}
