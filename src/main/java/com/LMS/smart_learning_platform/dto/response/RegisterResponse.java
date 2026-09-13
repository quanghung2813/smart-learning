package com.LMS.smart_learning_platform.dto.response;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    @Id
    long id;
    String username;
    String email;
    String passwordHash;
    Set<String> role;
}
