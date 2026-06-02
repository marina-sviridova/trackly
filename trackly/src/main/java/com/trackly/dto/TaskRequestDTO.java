package com.trackly.dto;

import com.trackly.model.TaskPriority;
import com.trackly.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDTO {
    @NotBlank
    String name;
    String description;
    @NotNull
    Long projectId;
    Long assigneeId;
    @NotNull
    TaskStatus status;
    @NotNull
    TaskPriority priority;
    LocalDateTime deadline;
}