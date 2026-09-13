package com.LMS.smart_learning_platform.configuration;

import com.LMS.smart_learning_platform.enums.Roles;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

// @Configuration báo cho Spring Boot biết đây là một class chứa các cấu hình hệ thống
@Configuration
// @EnableWebSecurity cho phép can thiệp và kiểm soát các request gửi đến ứng dụng
@EnableWebSecurity
// @EnableMethodSecurity cho phép phân quyền trực tiếp trên các hàm bên trong Controller
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {
            "/sign-in", "/register", "/refresh"
    };

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Bean
    // SecurityFilterChain để kiểm soát các request đi vào hệ thống
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {

        // authorizeHttpRequests là nơi thiết lập quyền truy cập cho từng URL cụ thể
        httpSecurity.authorizeHttpRequests(request ->

                                // requestMatchers(HttpMethod..., ...).permitAll() cho phép mọi người gửi request đến các đường dẫn
                        request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                                .requestMatchers(HttpMethod.GET, "/users").hasRole(Roles.STUDENT.name())
                                .requestMatchers(HttpMethod.GET, "/course").hasRole(Roles.INSTRUCTOR.name())
                        // anyRequest().authenticated() cần gửi token hợp lệ mới cho phép truy cập
                        .anyRequest().authenticated());

                    // oauth2ResourceServer để tiếp nhận và xác thực các jwt được gửi lên
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->

                        // jwtConfigurer.decoder(jwtDecoder()) để kiểm tra tính hợp lý của token
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        // csrf(AbstractHttpConfigurer::disable) để tắt tính năng bảo vệ chống giả mạo yêu cầu chéo trang
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // token của mình lưu role ở claim "role" (vd: "role": "STUDENT"),
        // trong khi mặc định JwtGrantedAuthoritiesConverter chỉ đọc claim "scope"/"scp"
        // => phải khai báo rõ claim "role" thì mới map ra được GrantedAuthority ROLE_STUDENT
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder  jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
       return NimbusJwtDecoder
               .withSecretKey(secretKeySpec)
               .macAlgorithm(MacAlgorithm.HS512)
               .build();
    }

    @Bean
    // mã hóa password
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
