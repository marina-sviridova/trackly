package com.trackly.controller;

import com.trackly.model.Role;
import com.trackly.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}/role")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<Void> changeUserRole(@PathVariable Long id, @RequestParam Role role) {
        userService.changeUserRole(id, role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}