package com.trackly.service;

import com.trackly.exception.UserNotFoundException;
import com.trackly.model.Role;
import com.trackly.model.User;
import com.trackly.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeUserRole(Long id, Role role) {
        User changedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        changedUser.setRole(role);
        userRepository.save(changedUser);
    }
}