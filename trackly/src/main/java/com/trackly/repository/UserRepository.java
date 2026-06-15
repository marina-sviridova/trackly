package com.trackly.repository;

import com.trackly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
}