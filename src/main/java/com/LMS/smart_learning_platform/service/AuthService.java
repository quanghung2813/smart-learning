package com.LMS.smart_learning_platform.service;

import com.LMS.smart_learning_platform.dto.request.LoginRequest;
import com.LMS.smart_learning_platform.dto.request.RefreshRequest;
import com.LMS.smart_learning_platform.dto.request.RegisterRequest;
import com.LMS.smart_learning_platform.dto.response.LoginResponse;
import com.LMS.smart_learning_platform.dto.response.RefreshResponse;
import com.LMS.smart_learning_platform.dto.response.RegisterResponse;
import com.LMS.smart_learning_platform.entity.User;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import com.LMS.smart_learning_platform.mapper.UserMapper;
import com.LMS.smart_learning_platform.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserRepository userRepository;
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public RegisterResponse register(RegisterRequest request) {

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));

        user = userRepository.save(user);


        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(Collections.singleton(request.getRole()))
                .build();
    }

    public RefreshResponse refresh(RefreshRequest request) throws JOSEException, ParseException {

        // Lấy chuỗi token mà người dùng gửi lên
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);

        return RefreshResponse.builder()
                .valid(verified)
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new  AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPasswordHash(), user.getPasswordHash());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(user);
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }

    String generateToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("LMS.com")
                .claim("userId", user.getId())
                .claim("role", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();

        } catch (JOSEException e) {
            log.error("Can't create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRole())) {
            user.getRole().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }
}
