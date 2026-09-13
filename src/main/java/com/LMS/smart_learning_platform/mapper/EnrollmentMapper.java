package com.LMS.smart_learning_platform.mapper;

import com.LMS.smart_learning_platform.dto.request.EnrollmentRequest;
import com.LMS.smart_learning_platform.entity.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentRequest request);
}
