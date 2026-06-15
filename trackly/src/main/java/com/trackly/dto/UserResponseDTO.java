package com.trackly.dto;

import com.trackly.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    Role role;
    Long managerId;
    LocalDateTime createdAt;
    boolean active;
}