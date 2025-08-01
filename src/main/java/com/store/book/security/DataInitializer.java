package com.store.book.security;

import com.store.book.dao.UserEntityRepository;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserEntityRepository userRepository;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                UserEntity adminUser = UserEntity.builder()
                        .userName("admin")
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@example.com")
                        .password(passwordEncoder().encode("admin"))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(adminUser);
            }
        };
    }

    @Bean(name = "encoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
