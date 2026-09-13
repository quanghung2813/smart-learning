package com.LMS.smart_learning_platform.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTED(1001,"Email existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002,"User not existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003,"Student not found", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1004, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1006, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    EMAIL_INVALID(1008, "email invalid", HttpStatus.BAD_REQUEST),
    NEED_SIGN_IN(1009, "Need to sign-in", HttpStatus.UNAUTHORIZED),
    COURSE_NOT_FOUND(1010, "Course not found", HttpStatus.NOT_FOUND),
    ENROLLMENT_EXISTED(1011, "Enrollment existed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    int code;
    String message;
    HttpStatus httpStatus;
}
