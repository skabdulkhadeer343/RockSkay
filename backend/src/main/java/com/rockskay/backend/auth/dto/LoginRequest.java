package com.rockskay.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "email is Required")
        @Email
        @Size(max = 255)
        String email,

        @NotBlank(message = "password is Required")
        @Size(
                min = 8,
                max = 50,
                message = "Password must be between 8 and 50 characters")
        String password
) {
}
