package com.LMS.smart_learning_platform.controller;

import com.LMS.smart_learning_platform.dto.request.ApiResponse;
import com.LMS.smart_learning_platform.dto.request.EnrollmentRequest;
import com.LMS.smart_learning_platform.dto.response.EnrollmentResponse;
import com.LMS.smart_learning_platform.entity.Enrollment;
import com.LMS.smart_learning_platform.service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping("/enrollment/{courseId}")
    ApiResponse<EnrollmentResponse> enrollment(
            @PathVariable("courseId") long courseId,
            @AuthenticationPrincipal Jwt jwt
            ) {

        var result = enrollmentService.enrollCourse(courseId, jwt.getClaim("userId"));

        return ApiResponse.<EnrollmentResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/my-enrollment")
    ApiResponse<List<EnrollmentResponse>> myEnrollment(@AuthenticationPrincipal Jwt jwt) {

        var result = enrollmentService.myEnrollment(jwt.getClaim("userId"));

        return ApiResponse.<List<EnrollmentResponse>>builder()
                .result(result.getResult())
                .build();
    }
}
