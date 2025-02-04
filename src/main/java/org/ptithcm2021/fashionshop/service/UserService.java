package org.ptithcm2021.fashionshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.UserRegisterRequest;
import org.ptithcm2021.fashionshop.dto.response.UserResponse;
import org.ptithcm2021.fashionshop.enums.RoleEnum;
import org.ptithcm2021.fashionshop.enums.UserStatusEnum;
import org.ptithcm2021.fashionshop.mapper.UserMapper;
import org.ptithcm2021.fashionshop.model.Role;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Transactional
    public UserResponse createUser(UserRegisterRequest userRegisterRequest) throws Exception {
        User user = userMapper.toUser(userRegisterRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatusEnum.PENDING);
        user.setRefreshToken(authenticationService.generateRefreshToken(user));
        user.setRole(Role.builder().role(RoleEnum.CUSTOMER).build());
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
