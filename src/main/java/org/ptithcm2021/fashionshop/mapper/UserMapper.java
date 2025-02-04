package org.ptithcm2021.fashionshop.mapper;

import org.mapstruct.Mapper;
import org.ptithcm2021.fashionshop.dto.request.UserRegisterRequest;
import org.ptithcm2021.fashionshop.dto.response.UserResponse;
import org.ptithcm2021.fashionshop.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest userRegisterRequest);
    UserResponse toUserResponse(User user);
}
