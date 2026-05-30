package com.trackly.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    String firstName;
    String lastName;
    String username;
    String password;
    String email;
}