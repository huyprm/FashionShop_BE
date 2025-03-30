package org.ptithcm2021.fashionshop.controller;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.LoginRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.AuthenticationResponse;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.ptithcm2021.fashionshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        AuthenticationResponse authenticationResponse = authenticationService.login(loginRequest);
        response.addCookie(authenticationResponse.getCookie());
        return ApiResponse.<String>builder()
                .data(authenticationResponse.getAccessToken())
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<String> refreshToken(@CookieValue (name = "refreshToken", required = false)String request, HttpServletResponse response, @RequestParam String userId) throws Exception {
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request, userId);

        response.addCookie(authenticationResponse.getCookie());
        return ApiResponse.<String>builder()
                .data(authenticationResponse.getAccessToken())
                .build();
    }

    @GetMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response) throws Exception {
        var authen= SecurityContextHolder.getContext().getAuthentication();

        response.addCookie(authenticationService.logout(authen.getName()));

        return ApiResponse.<String>builder().data("Success").build();
    }

    @GetMapping("/verifyEmail")
    public ApiResponse<String> verifyEmai(@RequestParam String token) {
        authenticationService.verificationEmail(token);
        return ApiResponse.<String>builder().data("Successfull authenticaiton").build();
    }

    @PostMapping("/verifyEmail/{email}")
    public ApiResponse<Void> sendVerifyEmai(@PathVariable String email) {
        authenticationService.sendVerificationEmail(email);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/sendOTP")
    public ApiResponse<String> sendOTP(@RequestParam String email){
        return ApiResponse.<String>builder().data(authenticationService.generateOTP(email)).build();
    }

    @PostMapping("/verifyOTP")
    public ApiResponse<Boolean> verifyOTP(@RequestParam String email, @RequestParam String otp){
        return ApiResponse.<Boolean>builder().data(authenticationService.verifyOTP(email, otp)).build();
    }
}
