package com.LMS.smart_learning_platform.controller;

import com.LMS.smart_learning_platform.dto.request.ApiResponse;
import com.LMS.smart_learning_platform.dto.request.LoginRequest;
import com.LMS.smart_learning_platform.dto.request.RefreshRequest;
import com.LMS.smart_learning_platform.dto.request.RegisterRequest;
import com.LMS.smart_learning_platform.dto.response.LoginResponse;
import com.LMS.smart_learning_platform.dto.response.RefreshResponse;
import com.LMS.smart_learning_platform.dto.response.RegisterResponse;
import com.LMS.smart_learning_platform.entity.User;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import com.LMS.smart_learning_platform.repository.UserRepository;
import com.LMS.smart_learning_platform.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
// @RestController báo cho Spring Boot rằng đây là một bộ điều khiển chuyên dùng để làm API
@RestController

// @RequiredArgsConstructor giúp tự động tạo hàm để truyền AuthService vào mà không cần phải viết thủ công
@RequiredArgsConstructor

// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) giúp tự động gán private final cho các biến bên trong class
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

//@RequestMapping("/api/v1")
public class AuthController {
    AuthService authService;
    UserRepository userRepository;

    // @PostMapping để map request vào logic hàm
    @PostMapping("/sign-in")

    // @RequestBody lấy dữ liệu người dùng gửi lên
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        var result = authService.login(request);

        // Đóng gói thông tin vừa nhận được từ Service vào ApiResponse rồi trả về cho client
        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/register")
    // @Valid kích hoạt kiểm tra dữ liệu
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        var result = authService.register(request);

        return ApiResponse.<RegisterResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {

        var result = authService.refresh(request);

        return ApiResponse.<RefreshResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/users")
    ApiResponse<List<User>> getUsers() {
        var result = userRepository.findAll();

        return ApiResponse.<List<User>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/user")
    ApiResponse<User> getUser(
//            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("id") long id) {
//        val userId = jwt.getClaim("userId");
        var result = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return ApiResponse.<User>builder()
                .result(result)
                .build();
    }
}
