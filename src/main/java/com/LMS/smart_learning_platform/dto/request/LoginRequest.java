package com.LMS.smart_learning_platform.dto.request;

import com.LMS.smart_learning_platform.exception.ErrorCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "EMAIL_INVALID")
    @Email
    String email;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String passwordHash;
}
