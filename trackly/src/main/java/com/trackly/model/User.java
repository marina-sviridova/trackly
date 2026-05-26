package com.trackly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue
    Long id;
    String firstName;
    String lastName;
    String username;
    String password;
    String email;
    @Enumerated(EnumType.STRING)
    Role role;
    @ManyToOne
    User manager;
    LocalDateTime createdAt;
    boolean isActive;
}