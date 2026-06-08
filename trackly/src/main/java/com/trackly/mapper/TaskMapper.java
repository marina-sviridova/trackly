package com.trackly.mapper;

import com.trackly.dto.TaskRequestDTO;
import com.trackly.dto.TaskResponseDTO;
import com.trackly.model.Project;
import com.trackly.model.Task;
import com.trackly.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper {

    public Task taskRequestDtoToTask(TaskRequestDTO taskRequestDTO,
                                     Project project,
                                     User createdBy,
                                     User assignee) {
        return Task.builder()
                .name(taskRequestDTO.getName())
                .description(taskRequestDTO.getDescription())
                .project(project)
                .createdBy(createdBy)
                .assignee(assignee)
                .status(taskRequestDTO.getStatus())
                .priority(taskRequestDTO.getPriority())
                .deadline(taskRequestDTO.getDeadline())
                .build();
    }

    public TaskResponseDTO taskToTaskResponseDto(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .projectId(task.getProject().getId())
                .createdById(task.getCreatedBy().getId())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .deadline(task.getDeadline())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}