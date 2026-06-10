package com.trackly.service;

import com.trackly.dto.ProjectRequestDTO;
import com.trackly.dto.ProjectResponseDTO;
import com.trackly.exception.ProjectNotFoundException;
import com.trackly.exception.UserNotFoundException;
import com.trackly.mapper.ProjectMapper;
import com.trackly.model.Project;
import com.trackly.model.User;
import com.trackly.repository.ProjectRepository;
import com.trackly.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO) {
        User manager = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<User> members = projectRequestDTO.getMembersIds() != null
                ? new ArrayList<>(projectRequestDTO.getMembersIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id)))
                .toList())
                : new ArrayList<>();
        Project project = projectMapper.projectRequestDtoToProject(projectRequestDTO, manager, members);
        return projectMapper.projectToProjectResponseDto(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO projectRequestDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        List<User> members = projectRequestDTO.getMembersIds() != null
                ? new ArrayList<>(projectRequestDTO.getMembersIds().stream()
                .map(memberId -> userRepository.findById(memberId)
                        .orElseThrow(() -> new UserNotFoundException(memberId)))
                .toList())
                : new ArrayList<>();
        project.setName(projectRequestDTO.getName());
        project.setDeadline(projectRequestDTO.getDeadline());
        project.setActive(projectRequestDTO.isActive());
        project.setMembers(members);
        return projectMapper.projectToProjectResponseDto(projectRepository.save(project));
    }

    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return projectMapper.projectToProjectResponseDto(project);
    }

    public Page<ProjectResponseDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(project -> projectMapper.projectToProjectResponseDto(project));
    }

    @Transactional
    public void deleteProjectById(Long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        projectRepository.deleteById(id);
    }
}