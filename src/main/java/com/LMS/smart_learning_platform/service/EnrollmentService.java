package com.LMS.smart_learning_platform.service;

import com.LMS.smart_learning_platform.dto.request.ApiResponse;
import com.LMS.smart_learning_platform.dto.response.EnrollmentResponse;
import com.LMS.smart_learning_platform.entity.Enrollment;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import com.LMS.smart_learning_platform.mapper.EnrollmentMapper;
import com.LMS.smart_learning_platform.repository.CourseRepository;
import com.LMS.smart_learning_platform.repository.EnrollmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentService {

    EnrollmentRepository enrollmentRepository;
    CourseRepository courseRepository;

    public EnrollmentResponse enrollCourse(
            long courseId,
            long userId
    ) {
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        var enrollmentCheck = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);

        if (!enrollmentCheck.isEmpty()) {
            throw new  AppException(ErrorCode.ENROLLMENT_EXISTED);
        }

        var enrollmentRequest = new Enrollment();
        enrollmentRequest.setUserId(userId);
        enrollmentRequest.setCourseId(courseId);

        Enrollment enrollment = enrollmentRepository.save(enrollmentRequest);

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(course.getId())
                .userId(enrollment.getUserId())
                .title(course.getTitle())
                .instructorId(course.getInstructorId())
                .status(course.getStatus())
                .build();
    }

    public ApiResponse<List<EnrollmentResponse>> myEnrollment(long userId) {

        var result = enrollmentRepository.myEnrollment(userId);
        if (result.isEmpty()) {
            throw new  AppException(ErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.<List<EnrollmentResponse>>builder()
                .result(result)
                .build();
    }
}
