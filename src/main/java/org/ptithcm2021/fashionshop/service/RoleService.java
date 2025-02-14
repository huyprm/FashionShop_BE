package org.ptithcm2021.fashionshop.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.fashionshop.dto.request.RoleRequest;
import org.ptithcm2021.fashionshop.dto.response.RoleResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.model.Role;
import org.ptithcm2021.fashionshop.repository.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleResponse getRole(String roleName) {

        Role role = roleRepository.findById(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return RoleResponse.builder()
                .description(role.getDescription())
                .role(role.getRole())
                .build();
    }

    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();

        List<RoleResponse> roleResponses = new ArrayList<>();
        for (Role role : roles) {
            roleResponses.add(RoleResponse.builder()
                    .role(role.getRole())
                    .description(role.getDescription())
                    .build());
        }
        return roleResponses;
    }

    public RoleResponse addRole(RoleRequest roleRequest) {
        if(roleRepository.findById(roleRequest.getRoleName()).isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        Role role = Role.builder()
                .role(roleRequest.getRoleName())
                .description(roleRequest.getRoleDescription())
                .build();

        roleRepository.save(role);
        return RoleResponse.builder()
                .role(role.getRole())
                .description(roleRequest.getRoleDescription())
                .build();
    }

    public String deleteRole(String roleName) {
        Role role = roleRepository.findById(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if(!role.getUsers().isEmpty()) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        roleRepository.deleteById(roleName);
        return "Role deleted";
    }
}
