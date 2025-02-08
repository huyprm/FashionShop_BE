package org.ptithcm2021.fashionshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String img;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String birthday;
    private String gender;
}
