package com.trackly.service;

import com.trackly.dto.UserResponseDTO;
import com.trackly.exception.UserNotFoundException;
import com.trackly.mapper.UserMapper;
import com.trackly.model.Role;
import com.trackly.model.User;
import com.trackly.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public void changeUserRole(Long id, Role role) {
        User changedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        changedUser.setRole(role);
        userRepository.save(changedUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> userMapper.userToUserResponseDto(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userMapper.userToUserResponseDto(currentUser);
    }
}