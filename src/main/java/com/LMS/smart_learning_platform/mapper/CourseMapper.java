package com.LMS.smart_learning_platform.mapper;

import com.LMS.smart_learning_platform.dto.request.CourseRequest;
import com.LMS.smart_learning_platform.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRequest request);
}
