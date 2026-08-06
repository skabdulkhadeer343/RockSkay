package com.rockskay.backend.user.dto;

import com.rockskay.backend.user.constants.UserRole;
import lombok.Builder;

import java.util.List;

@Builder
public record UserDto (

        String firstName,
        String lastName,
        String email,
        List<UserRole> roles
){
}
