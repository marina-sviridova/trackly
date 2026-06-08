package com.trackly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    Long id;
    Long createdBy;
    Long taskId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String message;
}