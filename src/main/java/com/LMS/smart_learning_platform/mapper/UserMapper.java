package com.LMS.smart_learning_platform.mapper;

import com.LMS.smart_learning_platform.dto.request.CourseRequest;
import com.LMS.smart_learning_platform.dto.request.RegisterRequest;
import com.LMS.smart_learning_platform.dto.response.RegisterResponse;
import com.LMS.smart_learning_platform.entity.Course;
import com.LMS.smart_learning_platform.entity.User;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);

    RegisterResponse toUserResponse(User user);

    // hỗ trợ tự động chuyển đổi String sang Set<String>
    default Set<String> map(String value) {
        if (value == null) {
            return null;
        }
        return Collections.singleton(value);
    }
}