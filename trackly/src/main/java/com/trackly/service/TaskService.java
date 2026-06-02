package com.trackly.service;

import com.trackly.dto.TaskRequestDTO;
import com.trackly.dto.TaskResponseDTO;
import com.trackly.exception.ProjectNotFoundException;
import com.trackly.exception.UserNotFoundException;
import com.trackly.mapper.TaskMapper;
import com.trackly.model.Project;
import com.trackly.model.Task;
import com.trackly.model.User;
import com.trackly.repository.ProjectRepository;
import com.trackly.repository.TaskRepository;
import com.trackly.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       TaskMapper taskMapper,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        Project project = projectRepository.findById(taskRequestDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(taskRequestDTO.getProjectId()));
        User createdBy = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User assignee = taskRequestDTO.getAssigneeId() != null
                ? userRepository.findById(taskRequestDTO.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException(taskRequestDTO.getAssigneeId()))
                : null;
        Task task = taskMapper.taskRequestDtoToTask(taskRequestDTO, project, createdBy, assignee);
        return taskMapper.taskToTaskResponseDto(taskRepository.save(task));
    }
}