package com.temirlan.firstspringproject.repository;

import com.temirlan.firstspringproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // No code needed! Spring generates everything at runtime
}