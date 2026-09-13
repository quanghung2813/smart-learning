package com.LMS.smart_learning_platform.repository;

import com.LMS.smart_learning_platform.dto.response.EnrollmentResponse;
import com.LMS.smart_learning_platform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsById(long id);
    List<Enrollment> findByUserId(long userId);
    List<Enrollment> findByUserIdAndCourseId(long userId, long courseId);

    @Query("""
    SELECT new com.LMS.smart_learning_platform.dto.response.EnrollmentResponse(
        e.id, c.id, e.userId, c.title, c.instructorId, c.status
    )
    FROM Enrollment e
    JOIN Course c ON e.courseId = c.id
    WHERE e.userId = :userId
    """)
    List<EnrollmentResponse> myEnrollment(@Param("userId") long userId);
}


