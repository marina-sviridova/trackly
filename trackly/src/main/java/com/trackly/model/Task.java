package com.trackly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue
    Long id;
    String name;
    String description;
    @ManyToOne
    Project project;
    @ManyToOne
    User createdBy;
    @ManyToOne
    User assignee;
    @Enumerated(EnumType.STRING)
    TaskStatus status;
    @Enumerated(EnumType.STRING)
    TaskPriority priority;
    @CreationTimestamp
    LocalDateTime createdAt;
    LocalDateTime deadline;
    @UpdateTimestamp
    LocalDateTime updatedAt;
    LocalDateTime completedAt;
}