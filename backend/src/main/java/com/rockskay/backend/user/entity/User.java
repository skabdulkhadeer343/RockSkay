package com.rockskay.backend.user.entity;

import com.rockskay.backend.auth.entity.RefreshToken;
import com.rockskay.backend.common.entity.SoftDeletableEntity;
import com.rockskay.backend.user.constants.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends SoftDeletableEntity {


    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Builder.Default
    @Column(name = "is_suspended", nullable = false)
    private boolean suspended = false;

    @Builder.Default
    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts = 0;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles", // Name of the auto-generated join table
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING) // Stores values as plain strings ("ROLE_USER", "ROLE_RECRUITER")
    @Column(name = "role_name", nullable = false, length = 30)
    private Set<UserRole> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    // ===========================
    // Convenience Methods
    // ===========================

    public void addRole(UserRole role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokens.add(refreshToken);
        refreshToken.setUser(this);
    }

}