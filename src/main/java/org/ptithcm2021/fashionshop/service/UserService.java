package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.UserChangePasswordRequest;
import org.ptithcm2021.fashionshop.dto.request.UserRegisterRequest;
import org.ptithcm2021.fashionshop.dto.request.UserUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.UserResponse;
import org.ptithcm2021.fashionshop.enums.RoleEnum;
import org.ptithcm2021.fashionshop.enums.UserStatusEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.UserMapper;
import org.ptithcm2021.fashionshop.model.Role;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public UserResponse createUser(UserRegisterRequest userRegisterRequest) throws Exception {
        User user = userMapper.toUser(userRegisterRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatusEnum.PENDING);
        user.setRefreshToken(authenticationService.generateRefreshToken(user));
        user.setRole(Role.builder().role(RoleEnum.CUSTOMER.toString()).build());
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("#id==authentication.name")
    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        if(userUpdateRequest.getFullname() != null) {
            user.setFullname(userUpdateRequest.getFullname());
        }
        if(userUpdateRequest.getEmail() != null) {
            user.setEmail(userUpdateRequest.getEmail());
        }
        if(userUpdateRequest.getPhone() != null) {
            user.setPhone(userUpdateRequest.getPhone());
        }
        if(userUpdateRequest.getAddress() != null) {
            user.setAddress(userUpdateRequest.getAddress());
        }
        if (userUpdateRequest.getGender() != null) {
            user.setGender(userUpdateRequest.getGender());
        }
        if(userUpdateRequest.getBirthday() != null) {
            user.setBirthday(userUpdateRequest.getBirthday());
        }

        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(UserStatusEnum.CANCELED);
        userRepository.save(user);
    }

    public UserResponse getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = new ArrayList<>();

        users.forEach(user -> userResponses.add(userMapper.toUserResponse(user)));

        return userResponses;
    }

    @PreAuthorize("#id==authentication.name")
    public String changePassword(UserChangePasswordRequest userChangePasswordRequest, String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
        } else throw new AppException(ErrorCode.WRONG_PASSWORD);

        userRepository.save(user);
        return "Changed password successfully";
    }
}
