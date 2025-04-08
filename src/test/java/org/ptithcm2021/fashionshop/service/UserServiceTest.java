package org.ptithcm2021.fashionshop.service;

import jakarta.inject.Inject;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.fashionshop.dto.request.UserChangePasswordRequest;
import org.ptithcm2021.fashionshop.dto.request.UserRegisterRequest;
import org.ptithcm2021.fashionshop.dto.request.UserUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.UserResponse;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.UserMapper;
import org.ptithcm2021.fashionshop.model.Role;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.RoleRepository;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private UserService userService;

    private UserRegisterRequest request;
    private User user;
    private User savedUser;
    private UserResponse userResponse;


    @BeforeEach
    void setup(){
        request = new UserRegisterRequest();
        request.setEmail("a@example.com");
        request.setPassword("123456");
        request.setFullname("Test User");

        user = new User();
        user.setPassword("123456");
        user.setFullname("Nguyen Van A");
        user.setEmail("a@example.com");
        user.setPhone("0323456789");

        savedUser = new User();
        savedUser.setId("123");
        savedUser.setEmail("a@example.com");
        savedUser.setPhone("0323456789");
        savedUser.setPassword("encodedPassword");
        savedUser.setFullname("Nguyen Van A");
        savedUser.setStatus(AccountStatusEnum.PENDING);
        savedUser.setRefreshToken("refreshtoken123");
        savedUser.setRole(Role.builder().role("CUSTOMER").build());

        userResponse = new UserResponse();
        userResponse.setEmail("a@example.com");
        userResponse.setFullname("Nguyen Van A");
        userResponse.setPhone("0323456789");
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        when(userMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(authenticationService.generateRefreshToken(any(User.class))).thenReturn("refreshtoken123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertEquals("a@example.com", result.getEmail());
        assertEquals("Nguyen Van A", result.getFullname());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterWithExistingEmail_ShouldThrowException() {
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> {
            userService.createUser(request);
        });

        assertEquals(ErrorCode.USER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void testGetUser_ShouldReturnCorrectUserInfo() {
        when(userRepository.findById("123")).thenReturn(Optional.of(savedUser));
        when(userMapper.toUserResponse(savedUser)).thenReturn(userResponse);

        UserResponse response = userService.getUser("123");

        assertEquals("Nguyen Van A", response.getFullname());
        assertEquals("a@example.com", response.getEmail());
        assertEquals("0323456789", response.getPhone());
    }

    @Test
    void testUpdateUser_ShouldUpdateInfoSuccessfully() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFullname("New Name");

        UserResponse newUser = new UserResponse();
        newUser.setId("123");
        newUser.setFullname("New Name");

        when(userRepository.findById("123")).thenReturn(Optional.of(savedUser));
        when(userRepository.save(savedUser)).thenAnswer(invocation ->invocation.getArgument(0));
        when(userMapper.toUserResponse(savedUser)).thenReturn(newUser);

        UserResponse response = userService.updateUser("123", request);

        assertEquals("New Name", response.getFullname());
    }

    @Test
    void changePassword_WithValidInfo_ShouldSucceed() {
        // Arrange
        UserChangePasswordRequest request = new UserChangePasswordRequest();
        request.setOldPassword("123456");      // Mật khẩu người dùng nhập
        request.setNewPassword("newSecurePassword123");  // Mật khẩu mới

        when(userRepository.findById("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedNewPassword");

        // Act
        String result = userService.changePassword(request, "123");

        // Assert
        assertEquals("Changed password successfully", result);
        verify(userRepository).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void changePassword_WithWrongOldPassword_ShouldThrowException() {
        // Arrange
        UserChangePasswordRequest request = new UserChangePasswordRequest();
        request.setOldPassword("12345");
        request.setNewPassword("newSecurePassword123");

        when(userRepository.findById("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            userService.changePassword(request, "123");
        });

        assertEquals(ErrorCode.WRONG_PASSWORD, exception.getErrorCode());
    }
}
