package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.LoginRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.AuthenticationResponse;
import org.ptithcm2021.fashionshop.enums.ErrorCode;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.ptithcm2021.fashionshop.service.AuthenticationService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Security;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        AuthenticationResponse authenticationResponse = authenticationService.login(loginRequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody String refreshToken, String mail) throws Exception {

        if (!authenticationService.verifyToken(refreshToken))
            throw new AppException(ErrorCode.INVALID_JWT);
        User user = userRepository.findByEmail(mail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //check token revoked
        if(!user.getRefreshToken().equals(refreshToken)) throw new AppException(ErrorCode.INVALID_JWT);

        String refresh = authenticationService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(authenticationService.generateAccessToken(user))
                .refreshToken(refresh)
                .build();

        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .build();
    }
    @GetMapping("/logout")
    public ApiResponse<String> logout() throws Exception {
        var authen= SecurityContextHolder.getContext().getAuthentication();
        authenticationService.handleRefreshToken(authen.getName());
        return ApiResponse.<String>builder().data("Success").build();
    }


public ApiResponse<Void> changePassword(String oldPassword, String newPassword) {

    return null;
}
public ApiResponse<String> register(String username, String password) {
    return null;
}

public ApiResponse<String> verifyEmai(LoginRequest loginRequest) {
    return null;
}
}
