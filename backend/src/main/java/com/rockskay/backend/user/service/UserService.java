package com.rockskay.backend.user.service;

import com.rockskay.backend.auth.dto.RegisterRequest;
import com.rockskay.backend.common.exception.resource.ResourceNotFoundException;
import com.rockskay.backend.common.util.EmailUtil;
import com.rockskay.backend.user.constants.UserRole;
import com.rockskay.backend.user.dto.UserDto;
import com.rockskay.backend.user.entity.User;
import com.rockskay.backend.user.mapper.UserMapper;
import com.rockskay.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public boolean existsByEmail(String email) {

        return userRepository.existsByEmail(
                EmailUtil.normalize(email)
        );
    }

    public User createUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(EmailUtil.normalize(request.email()))
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        user.addRole(UserRole.ROLE_USER);

        UserRole requestedRoleEnum = request.role();
        if (requestedRoleEnum != null) {
            if (requestedRoleEnum == UserRole.ROLE_ADMIN) user.addRole(UserRole.ROLE_ADMIN);
            if (requestedRoleEnum == UserRole.ROLE_RECRUITER) user.addRole(UserRole.ROLE_RECRUITER);
        }

        return userRepository.save(user);
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(
                        EmailUtil.normalize(email)
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        ));
    }

    public UserDto findById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        ));

        return userMapper.toDto(user);
    }

    public void verifyUser(String email) {

        User user = findByEmail(email);

        user.setVerified(true);

        userRepository.save(user);
    }
}