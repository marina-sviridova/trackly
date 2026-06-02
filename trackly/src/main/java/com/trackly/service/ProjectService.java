package com.trackly.service;

import com.trackly.dto.ProjectRequestDTO;
import com.trackly.dto.ProjectResponseDTO;
import com.trackly.exception.UserNotFoundException;
import com.trackly.mapper.ProjectMapper;
import com.trackly.model.Project;
import com.trackly.model.User;
import com.trackly.repository.ProjectRepository;
import com.trackly.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO) {
        User manager = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<User> members = projectRequestDTO.getMembersIds() != null
                ? projectRequestDTO.getMembersIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id)))
                .toList()
                : List.of();
        Project project = projectMapper.projectRequestDtoToProject(projectRequestDTO, manager, members);
        return projectMapper.projectToProjectResponseDto(projectRepository.save(project));
    }
}