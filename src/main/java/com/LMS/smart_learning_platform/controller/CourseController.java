package com.LMS.smart_learning_platform.controller;

import com.LMS.smart_learning_platform.dto.request.ApiResponse;
import com.LMS.smart_learning_platform.dto.request.CourseRequest;
import com.LMS.smart_learning_platform.dto.response.CourseResponse;
import com.LMS.smart_learning_platform.entity.Course;
import com.LMS.smart_learning_platform.entity.User;
import com.LMS.smart_learning_platform.enums.Status;
import com.LMS.smart_learning_platform.enums.Types;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import com.LMS.smart_learning_platform.repository.CourseRepository;
import com.LMS.smart_learning_platform.service.CourseService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class CourseController {

    CourseService courseService;

    @PostMapping("/course")
    ApiResponse<CourseResponse> courses(
            @RequestBody @Valid CourseRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        // jwt.getClaim("userId") dùng để id của người dùng từ jwt
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.course(request, jwt.getClaim("userId")))
                .build();
    }

    @GetMapping("/courses")
    ApiResponse<List<Course>> courses(
            @Pattern(regexp = "^(all|draft|published)$", message = "Type must be all, draft or published")
            @RequestParam("Type") String type
    ) {

        return ApiResponse.<List<Course>>builder()
                .result(courseService.type(type).getResult())
                .build();
    }

    @GetMapping("/course")
    ApiResponse<Course> getCourses(
            @RequestParam("id") long id
    ) {

        return ApiResponse.<Course>builder()
                .result(courseService.findById(id).getResult())
                .build();
    }
}
