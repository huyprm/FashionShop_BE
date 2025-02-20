package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.UserChangePasswordRequest;
import org.ptithcm2021.fashionshop.dto.request.UserRegisterRequest;
import org.ptithcm2021.fashionshop.dto.request.UserUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.UserResponse;
import org.ptithcm2021.fashionshop.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.load;

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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    return ApiResponse.<Void>builder().message("Successfull").build();
    }

    @PutMapping("/{id}/change_password")
    public ApiResponse<Void> changePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest,
                                            @PathVariable String id) {
        return ApiResponse.<Void>builder().message(userService.changePassword(userChangePasswordRequest, id)).build();
    }

    @PutMapping("/{id}/change_role")
    public ApiResponse<UserResponse> changeRoleUser(@PathVariable String id, @RequestParam String role) {
        return ApiResponse.<UserResponse>builder().data(userService.changeRoleUser(id, role)).build();
    }

    @PostMapping("/{id}/avatar")
    public ApiResponse<UserResponse> updateUserAvatar(@PathVariable String id, @RequestParam MultipartFile file) throws UnsupportedEncodingException {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateAvatar(file, id))
                .build();

    }

    @GetMapping("/get-avatar/{fileName}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String fileName) throws FileNotFoundException {
        Path file = Paths.get("E:/Java/FashionShop/images/avatar/").resolve(fileName);
        File temp = new File(file.toFile().getAbsolutePath());

        // Wrap the File in InputStreamResource or FileSystemResource
        InputStreamResource resource = new InputStreamResource(new FileInputStream(temp));

        // Check if the file exists and is readable
        if (temp.exists() && temp.canRead()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        }

        // Return an error response if the file does not exist or cannot be read
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

}
