package com.LMS.smart_learning_platform.service;

import com.LMS.smart_learning_platform.dto.request.ApiResponse;
import com.LMS.smart_learning_platform.dto.request.CourseRequest;
import com.LMS.smart_learning_platform.dto.response.CourseResponse;
import com.LMS.smart_learning_platform.enums.Status;
import com.LMS.smart_learning_platform.enums.Types;
import com.LMS.smart_learning_platform.exception.AppException;
import com.LMS.smart_learning_platform.exception.ErrorCode;
import com.LMS.smart_learning_platform.mapper.CourseMapper;
import com.LMS.smart_learning_platform.entity.Course;
import com.LMS.smart_learning_platform.repository.CourseRepository;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {

    CourseRepository courseRepository;
    CourseMapper courseMapper;

    public CourseResponse course(CourseRequest request, long instructorId) {

        var courseRequest = courseMapper.toCourse(request);

        // cập nhật id của giảng viên vào khóa học được tạo
        courseRequest.setInstructorId(instructorId);

        Course course = courseRepository.save(courseRequest);

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .instructorId(course.getInstructorId())
                .status(course.getStatus())
                .build();
    }

    public ApiResponse<List<Course>> type (String type) {

        var result = switch (type) {
            case "all" -> courseRepository.findAll();
            case "draft" -> courseRepository.findByStatus(Status.DRAFT);
            case "published" -> courseRepository.findByStatus(Status.PUBLISHED);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        return  ApiResponse.<List<Course>>builder()
                .result(result)
                .build();
    }

    public ApiResponse<Course> findById(long id) {

        var result = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        return  ApiResponse.<Course>builder()
                .result(result)
                .build();
    }
}
