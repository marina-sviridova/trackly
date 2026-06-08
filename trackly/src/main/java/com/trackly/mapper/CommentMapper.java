package com.trackly.mapper;

import com.trackly.dto.CommentRequestDTO;
import com.trackly.dto.CommentResponseDTO;
import com.trackly.model.Comment;
import com.trackly.model.Task;
import com.trackly.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public Comment commentRequestDtoToComment(CommentRequestDTO commentRequestDTO,
                                              User createdBy,
                                              Task task) {
        return Comment.builder()
                .createdBy(createdBy)
                .task(task)
                .message(commentRequestDTO.getMessage())
                .build();
    }

    public CommentResponseDTO commentToCommentResponseDto(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .createdBy(comment.getCreatedBy().getId())
                .taskId(comment.getTask().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .message(comment.getMessage())
                .build();
    }
}