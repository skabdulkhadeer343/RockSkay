package com.rockskay.backend.user.mapper;

import com.rockskay.backend.user.constants.UserRole;
import com.rockskay.backend.user.dto.UserDto;
import com.rockskay.backend.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.firstName(user.getFirstName());
        userDto.lastName(user.getLastName());
        userDto.email(user.getEmail());
        Set<UserRole> set = user.getRoles();
        if (set != null) {
            userDto.roles(new ArrayList<UserRole>(set));
        }

        return userDto.build();
    }
}