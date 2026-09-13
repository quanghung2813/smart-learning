package com.LMS.smart_learning_platform.repository;

import com.LMS.smart_learning_platform.entity.Role;
import com.LMS.smart_learning_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
