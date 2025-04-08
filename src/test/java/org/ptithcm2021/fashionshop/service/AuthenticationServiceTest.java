package org.ptithcm2021.fashionshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.LoginRequest;
import org.ptithcm2021.fashionshop.dto.response.AuthenticationResponse;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.model.Role;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.ptithcm2021.fashionshop.util.MailUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
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
        Role role = new Role();
        role.setRole("ADMIN");

        mockUser = new User();
        mockUser.setId("123");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setStatus(AccountStatusEnum.ACTIVE);
        mockUser.setRole(role);

        ReflectionTestUtils.setField(authenticationService, "sign", "Nl0EwgZ2zY7r2lfXYJOUguERWZkvn3KDPu6pK+9EWFMsqCfywwzmhViCGawL2qQN");
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
    void testLoginWithInvalidEmail() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("invalidPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.login(loginRequest);
        });
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testLoginWithPendingAccount_ShouldThrowAccountLocked() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setUsername("user@example.com");
        request.setPassword("123");

        User pendingUser = new User();
        pendingUser.setEmail("user@example.com");
        pendingUser.setPassword("123");
        pendingUser.setStatus(AccountStatusEnum.PENDING);

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(pendingUser));
        when(passwordEncoder.matches("123", "123")).thenReturn(true);

        // When + Then
        AppException ex = assertThrows(AppException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals(ErrorCode.ACCOUNT_LOCKED, ex.getErrorCode());
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

