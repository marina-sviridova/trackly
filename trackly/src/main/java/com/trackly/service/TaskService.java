package com.trackly.service;

import com.trackly.dto.TaskRequestDTO;
import com.trackly.dto.TaskResponseDTO;
import com.trackly.exception.ProjectNotFoundException;
import com.trackly.exception.TaskNotFoundException;
import com.trackly.exception.UserNotFoundException;
import com.trackly.mapper.TaskMapper;
import com.trackly.model.Project;
import com.trackly.model.Task;
import com.trackly.model.User;
import com.trackly.repository.ProjectRepository;
import com.trackly.repository.TaskRepository;
import com.trackly.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
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

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        Project project = projectRepository.findById(taskRequestDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(taskRequestDTO.getProjectId()));
        User assignee = taskRequestDTO.getAssigneeId() != null
                ? userRepository.findById(taskRequestDTO.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException(taskRequestDTO.getAssigneeId()))
                : null;
        Task updatedTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        updatedTask.setName(taskRequestDTO.getName());
        updatedTask.setDescription(taskRequestDTO.getDescription());
        updatedTask.setProject(project);
        updatedTask.setAssignee(assignee);
        updatedTask.setStatus(taskRequestDTO.getStatus());
        updatedTask.setPriority(taskRequestDTO.getPriority());
        updatedTask.setDeadline(taskRequestDTO.getDeadline());
        updatedTask.setUpdatedAt(LocalDateTime.now());
        return taskMapper.taskToTaskResponseDto(taskRepository.save(updatedTask));
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.taskToTaskResponseDto(task);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> taskMapper.taskToTaskResponseDto(task))
                .toList();
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.deleteById(id);
    }
}