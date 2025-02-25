package org.ptithcm2021.fashionshop.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

import static org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {
    @Value("${jwt.signerKey}")
    private String signKey;
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/verifyEmail/{email}",
            "/api/users/register",
            "/api/payments/returnStatus/**"
    };

    private static final String[] GET_ENDPOINTS = {
            "/api/products/**",
            "/api/brands/**",
            "/api/categories/**",
            "/api/auth/verifyEmail",
            "/api/images/**",
            "/api/bundle_discounts"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",  // Swagger UI
                                "/v3/api-docs/**", // OpenAPI docs
                                "/webjars/**",     // Swagger webjars
                                "/swagger-resources/**").permitAll()
                        .anyRequest().authenticated()
                );

//        http.cors(cors -> cors.configurationSource(request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(List.of("http://localhost:3000")); // Chỉ frontend này được truy cập
//            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//            config.setAllowedHeaders(List.of("*"));
//            config.setAllowCredentials(true);
//            return config;
//        }));

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                .authenticationEntryPoint(new EntryPointAuthentication())
                );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(signKey.getBytes(), "HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();

        return token -> {
            try {
                return nimbusJwtDecoder.decode(token);
            } catch (Exception e) {
                throw new JwtException("Invalid JWT token", e);
            }
        };
    }


}
