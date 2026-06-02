package com.trackly.mapper;

import com.trackly.dto.ProjectRequestDTO;
import com.trackly.dto.ProjectResponseDTO;
import com.trackly.model.Project;
import com.trackly.model.Task;
import com.trackly.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProjectMapper {
    public Project projectRequestDtoToProject (ProjectRequestDTO projectRequestDTO, User manager, List<User> members) {

        return Project.builder()
                .name(projectRequestDTO.getName())
                .manager(manager)
                .createdAt(LocalDateTime.now())
                .deadline(projectRequestDTO.getDeadline())
                .members(members)
                .active(projectRequestDTO.isActive())
                .build();
    }

    public ProjectResponseDTO projectToProjectResponseDto(Project project) {
        return  ProjectResponseDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .managerId(project.getManager().getId())
                .createdAt(project.getCreatedAt())
                .deadline(project.getDeadline())
                .membersIds(project.getMembers() != null
                        ? project.getMembers().stream().map(User::getId).toList()
                        : List.of())
                .tasksIds(project.getTasks() != null
                        ? project.getTasks().stream().map(Task::getId).toList()
                        : List.of())
                .active(project.isActive())
                .build();
    }
}