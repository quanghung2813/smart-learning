package com.LMS.smart_learning_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {

    @NotBlank(message = "Title is empty")
    String title;

    @NotBlank(message = "Status is empty")
    @Pattern(regexp = "^(DRAFT|PUBLISHED)$", message = "Status must be DRAFT or PUBLISHED.")
    String status;
}
