package com.trackly.specification;

import com.trackly.model.Task;
import com.trackly.model.TaskPriority;
import com.trackly.model.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskSpecification {

    public static Specification<Task> hasProject(Long projectId) {
        return (root, query, cb) ->
                projectId == null ? null : cb.equal(root.get("project").get("id"), projectId);
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ? null : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) ->
                priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> deadlineBefore(LocalDateTime deadline) {
        return (root, query, cb) ->
                deadline == null ? null : cb.lessThanOrEqualTo(root.get("deadline"), deadline);
    }
}