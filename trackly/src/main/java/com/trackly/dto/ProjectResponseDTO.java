package com.trackly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponseDTO {
    Long id;
    String name;
    Long managerId;
    LocalDateTime createdAt;
    LocalDateTime deadline;
    List<Long> tasksIds;
    List<Long> membersIds;
    boolean active;
}