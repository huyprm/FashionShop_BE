package org.ptithcm2021.fashionshop.configure;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.enums.RoleEnum;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.model.Role;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.ptithcm2021.fashionshop.repository.RoleRepository;
import org.ptithcm2021.fashionshop.service.AuthenticationService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitializeData {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Bean
    @Transactional
    ApplicationRunner init() {
        return args -> {
            // Initialize roles
            if (roleRepository.count() == 0) {
                for (RoleEnum roleEnum : RoleEnum.values()) {
                        Role role = new Role();
                        role.setRole(roleEnum.toString());
                        role.setDescription(roleEnum.getDescription());
                        roleRepository.save(role);
                }
            }

            if (userRepository.findByEmail("huydlx@gmail.com").isEmpty()) {
                Role adminRole = roleRepository.findById(RoleEnum.ADMIN.toString())
                        .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

                User user = new User();
                user.setEmail("huydlx@gmail.com");
                user.setFullname("MyAdmin");
                user.setRefreshToken(authenticationService.generateRefreshToken(user));
                user.setPassword(passwordEncoder.encode("12345"));
                user.setRole(adminRole);
                userRepository.save(user);
            }
        };
    }
}
