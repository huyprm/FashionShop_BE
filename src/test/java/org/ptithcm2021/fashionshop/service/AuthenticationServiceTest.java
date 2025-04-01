package org.ptithcm2021.fashionshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.ptithcm2021.fashionshop.dto.request.LoginRequest;
import org.ptithcm2021.fashionshop.dto.response.AuthenticationResponse;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.ptithcm2021.fashionshop.util.MailUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId("123");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setStatus(AccountStatusEnum.ACTIVE);
    }

    @Test
    void testLoginWithValidEmail() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("validPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("validPassword", "encodedPassword")).thenReturn(true);

        // Act
        AuthenticationResponse response = authenticationService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getCookie());
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("invalidPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("invalidPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.login(loginRequest);
        });
        assertEquals(ErrorCode.WRONG_PASSWORD, exception.getErrorCode());
    }

    @Test
    void testGenerateAccessToken() throws Exception {
        // Act
        String accessToken = authenticationService.generateAccessToken(mockUser);

        // Assert
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith("eyJ"));
    }

    @Test
    public void testGenerateOTP() {
        String email = "test@example.com";
        String generatedOtp = "123456";

        // Mock the redisTemplate to simulate setting the OTP in Redis
        doNothing().when(redisTemplate).opsForValue().set(eq("otp:" + email), anyString(), any(Duration.class));

        // Call the method
        String otp = authenticationService.generateOTP(email);

        // Assert that the OTP generated is the one we expect
        assertEquals(generatedOtp, otp);

        // Verify that redisTemplate's set method was called with the correct parameters
        verify(redisTemplate).opsForValue().set(eq("otp:" + email), anyString(), any(Duration.class));
    }

    @Test
    void testVerifyOTP() {
        // Arrange
        String email = "test@example.com";
        String otp = "123456";
        when(redisTemplate.opsForValue().get("otp:" + email)).thenReturn("123456");

        // Act
        boolean isVerified = authenticationService.verifyOTP(email, otp);

        // Assert
        assertTrue(isVerified);
    }

    @Test
    void testLogout() {
        // Arrange
        String userId = "123";
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Cookie cookie = authenticationService.logout(userId);

        // Assert
        assertNotNull(cookie);
        assertEquals(0, cookie.getMaxAge()); // Cookie should be expired
    }
}

