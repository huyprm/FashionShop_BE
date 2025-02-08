package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.UserChangePasswordRequest;
import org.ptithcm2021.fashionshop.dto.request.UserRegisterRequest;
import org.ptithcm2021.fashionshop.dto.request.UserUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.UserResponse;
import org.ptithcm2021.fashionshop.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ApiResponse<UserResponse> registerAccount(@RequestBody @Valid UserRegisterRequest userRegisterRequest) throws Exception {
        return ApiResponse.<UserResponse>builder().data(userService.createUser(userRegisterRequest)).build();
    }

    @GetMapping()
    public ApiResponse<List<UserResponse>> getAllUsers() throws Exception {

        return ApiResponse.<List<UserResponse>>builder().data(userService.getAllUsers()).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) throws Exception {
        return ApiResponse.<UserResponse>builder().data(userService.getUser(id)).build();
    }

    @GetMapping("/account")
    public ApiResponse<UserResponse> getUserLogin() throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<UserResponse>builder().data(userService.getUser(id)).build();
    }

    @PatchMapping("/{id}/update")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id,
                                        @RequestBody @Valid UserUpdateRequest userUpdateRequest) {

        return ApiResponse.<UserResponse>builder().data( userService.updateUser(id, userUpdateRequest)).build();
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    return ApiResponse.<Void>builder().message("Successfull").build();
    }

    @PostMapping("/{id}/change_password")
    public ApiResponse<Void> ChangePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest,
                                            @PathVariable String id) {
        return ApiResponse.<Void>builder().message(userService.changePassword(userChangePasswordRequest, id)).build();
    }
}
