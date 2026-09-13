package com.LMS.smart_learning_platform.service;

import com.LMS.smart_learning_platform.dto.request.RegisterRequest;
import com.LMS.smart_learning_platform.dto.response.RegisterResponse;
import com.LMS.smart_learning_platform.entity.User;
import com.LMS.smart_learning_platform.enums.Roles;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import com.LMS.smart_learning_platform.mapper.UserMapper;
import com.LMS.smart_learning_platform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public RegisterResponse registerRequest(RegisterRequest request) {

        // kiểm tra email đã tồn tại hay chưa
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));

        HashSet<String> role = new HashSet<>();
        // mặc định gán role STUDENT
        role.add(Roles.STUDENT.name());
        user.setRole(role);

        // lưu thông tin vào database
        return userMapper.toUserResponse(userRepository.save(user));
    }

    // @PreAuthorize("hasRole('INSTRUCTOR')") kiểm tra phải có role là INSTRUCTOR thì mới cho phép gọi hàm
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<RegisterResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public RegisterResponse getUser(String email) {
        return userMapper.toUserResponse(userRepository.findById(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
}
