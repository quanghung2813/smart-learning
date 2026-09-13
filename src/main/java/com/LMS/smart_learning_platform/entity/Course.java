package com.LMS.smart_learning_platform.entity;

import com.LMS.smart_learning_platform.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Course {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) tự động tăng id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String title;
    long instructorId;
    Status status;
}
