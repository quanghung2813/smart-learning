package com.LMS.smart_learning_platform.dto.response;

import com.LMS.smart_learning_platform.enums.Status;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentResponse {
    @Id
    long id;
    long courseId;
    long userId;

    String title;
    long instructorId;
    Status status;
}
