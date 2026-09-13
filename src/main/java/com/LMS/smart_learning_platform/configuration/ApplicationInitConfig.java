package com.LMS.smart_learning_platform.configuration;

import com.LMS.smart_learning_platform.entity.User;
import com.LMS.smart_learning_platform.enums.Roles;
import com.LMS.smart_learning_platform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    @Bean
    // applicationRunner sẽ được khởi chạy mỗi khi application start
    ApplicationRunner applicationRunner(UserRepository userRepository) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return args -> {
            // kiểm tra đã có admin hay chưa
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Roles.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .passwordHash(passwordEncoder.encode("password"))
                        .role(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: password, please change it");
            }
        };
    }
}
