package com.trackly.dto;

import com.trackly.model.TaskPriority;
import com.trackly.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDTO {
    Long id;
    String name;
    String description;
    Long projectId;
    Long createdById;
    Long assigneeId;
    TaskStatus status;
    TaskPriority priority;
    LocalDateTime createdAt;
    LocalDateTime deadline;
    LocalDateTime updatedAt;
    LocalDateTime completedAt;
}