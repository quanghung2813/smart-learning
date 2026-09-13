package com.LMS.smart_learning_platform.repository;

import com.LMS.smart_learning_platform.entity.Course;
import com.LMS.smart_learning_platform.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    boolean existsById(long id);
    Optional<Course> findById(long id);
    List<Course> findByStatus(Status status);
}
