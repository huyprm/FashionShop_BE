package org.ptithcm2021.fashionshop.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.RoleRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.RoleResponse;
import org.ptithcm2021.fashionshop.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String id) {
        return ApiResponse.<RoleResponse>builder().data(roleService.getRole(id)).build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder().data(roleService.getRoles()).build();
    }

    @PostMapping("/addRole")
    public ApiResponse<RoleResponse> addRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder().data(roleService.addRole(roleRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRole(@PathVariable String id) {
        return ApiResponse.<String>builder().data(roleService.deleteRole(id)).build();
    }
}
