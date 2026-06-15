package com.trackly.service;

import com.trackly.dto.TaskRequestDTO;
import com.trackly.dto.TaskResponseDTO;
import com.trackly.exception.ProjectNotFoundException;
import com.trackly.exception.TaskNotFoundException;
import com.trackly.exception.UserNotFoundException;
import com.trackly.mapper.TaskMapper;
import com.trackly.model.*;
import com.trackly.repository.ProjectRepository;
import com.trackly.repository.TaskRepository;
import com.trackly.repository.UserRepository;
import com.trackly.specification.TaskSpecification;
import org.springframework.transaction.annotation.Transactional ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        if (assignee != null && !project.getMembers().contains(assignee)) {
            throw new IllegalArgumentException(
                    "User " + assignee.getUsername() + " is not a member of project " + project.getId());
        }
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
        if (assignee != null && !project.getMembers().contains(assignee)) {
            throw new IllegalArgumentException(
                    "User " + assignee.getUsername() + " is not a member of project " + project.getId());
        }
        Task updatedTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        updatedTask.setName(taskRequestDTO.getName());
        updatedTask.setDescription(taskRequestDTO.getDescription());
        updatedTask.setProject(project);
        updatedTask.setAssignee(assignee);
        updatedTask.setStatus(taskRequestDTO.getStatus());
        if (updatedTask.getStatus().equals(TaskStatus.DONE)) {
            updatedTask.setCompletedAt(LocalDateTime.now());
        } else {
            updatedTask.setCompletedAt(null);
        }
        updatedTask.setPriority(taskRequestDTO.getPriority());
        updatedTask.setDeadline(taskRequestDTO.getDeadline());
        return taskMapper.taskToTaskResponseDto(taskRepository.save(updatedTask));
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.taskToTaskResponseDto(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getTasks(Long projectId, Long assigneeId, TaskStatus status, TaskPriority priority,
                                          LocalDateTime deadline, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.hasProject(projectId)
                .and(TaskSpecification.hasAssignee(assigneeId))
                .and(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.deadlineBefore(deadline));
        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::taskToTaskResponseDto);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.deleteById(id);
    }
}