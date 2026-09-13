package com.LMS.smart_learning_platform.entity;

import com.LMS.smart_learning_platform.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {
    @Id
    long id;
    String name;
    Roles roles;
}
