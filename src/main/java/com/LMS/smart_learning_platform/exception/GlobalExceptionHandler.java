package com.LMS.smart_learning_platform.exception;

import com.LMS.smart_learning_platform.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// dùng để hấng và viết ra các lỗi
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    ResponseEntity<ApiResponse> handleValidation(Throwable e) {
        e.printStackTrace();
        String defaultMessage = "Validation error";
        if (e.getMessage() != null) {
            defaultMessage = e.getMessage();
        }

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(400) // Hoặc mã lỗi tùy chỉnh trong ErrorCode của bạn
                .message(defaultMessage)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
