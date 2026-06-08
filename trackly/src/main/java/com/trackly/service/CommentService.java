package com.trackly.service;

import com.trackly.dto.CommentRequestDTO;
import com.trackly.dto.CommentResponseDTO;
import com.trackly.exception.CommentNotFoundException;
import com.trackly.exception.TaskNotFoundException;
import com.trackly.mapper.CommentMapper;
import com.trackly.model.Comment;
import com.trackly.model.Task;
import com.trackly.model.User;
import com.trackly.repository.CommentRepository;
import com.trackly.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        User createdBy = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Task task = taskRepository.findById(commentRequestDTO.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(commentRequestDTO.getTaskId()));
        Comment createdComment = commentMapper.commentRequestDtoToComment(commentRequestDTO, createdBy, task);
        return commentMapper.commentToCommentResponseDto(commentRepository.save(createdComment));
    }

    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        comment.setMessage(commentRequestDTO.getMessage());
        return commentMapper.commentToCommentResponseDto(commentRepository.save(comment));
    }

    public CommentResponseDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return commentMapper.commentToCommentResponseDto(comment);
    }

    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(comment -> commentMapper.commentToCommentResponseDto(comment))
                .toList();
    }

    public void deleteCommentById(Long id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        commentRepository.deleteById(id);
    }
}