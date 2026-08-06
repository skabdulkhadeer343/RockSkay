package com.rockskay.backend.auth.dto;

import com.rockskay.backend.user.constants.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterRequest (

        @NotBlank(message = "First Name is requried")
        String firstName,

        @NotBlank(message = "Last Name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(
        min = 8,
        max = 100,
        message = "Password must be between 8 and 100 characters")
        String password,

        UserRole role

){}