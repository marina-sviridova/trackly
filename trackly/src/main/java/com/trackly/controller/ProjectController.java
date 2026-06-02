package com.trackly.controller;

import com.trackly.dto.ProjectRequestDTO;
import com.trackly.dto.ProjectResponseDTO;
import com.trackly.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController (ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        return new ResponseEntity<>(projectService.createProject(projectRequestDTO), HttpStatus.CREATED);
    }
}