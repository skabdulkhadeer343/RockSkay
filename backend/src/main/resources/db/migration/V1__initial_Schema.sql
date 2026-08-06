-- ============================================================================
-- RockSkay - V1 Initial Schema (Aligned with Java Entity Layer)
-- Authentication & Authorization
-- PostgreSQL
-- ============================================================================

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================================
-- USERS
-- ============================================================================

CREATE TABLE users (
                       id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       first_name          VARCHAR(50) NOT NULL,
                       last_name           VARCHAR(50) NOT NULL, -- Perfectly aligned with your entity property!
                       email               VARCHAR(255) NOT NULL UNIQUE,
                       password_hash       VARCHAR(255) NOT NULL,

                       is_verified         BOOLEAN NOT NULL DEFAULT FALSE,
                       is_suspended        BOOLEAN NOT NULL DEFAULT FALSE,

                       failed_attempts     INTEGER NOT NULL DEFAULT 0,
                       locked_until        TIMESTAMPTZ,

                       last_login_at       TIMESTAMPTZ,

    -- Audit fields inherited from SoftDeletableEntity / AuditableEntity
                       created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       created_by          UUID,

                       updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_by          UUID,

                       deleted_at          TIMESTAMPTZ,
                       deleted_by          UUID,

                       CONSTRAINT fk_users_created_by
                           FOREIGN KEY (created_by)
                               REFERENCES users(id),

                       CONSTRAINT fk_users_updated_by
                           FOREIGN KEY (updated_by)
                               REFERENCES users(id),

                       CONSTRAINT fk_users_deleted_by
                           FOREIGN KEY (deleted_by)
                               REFERENCES users(id)
);

-- ============================================================================
-- USER ROLES (Maps to Hibernate @ElementCollection)
-- ============================================================================

CREATE TABLE user_roles (
                            user_id             UUID NOT NULL,
                            role_name           VARCHAR(30) NOT NULL, -- Aligned with length = 30 and non-nullable status

                            PRIMARY KEY (user_id, role_name),

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE
);

-- ============================================================================
-- REFRESH TOKENS
-- ============================================================================

CREATE TABLE refresh_tokens (
                                id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id             UUID NOT NULL,
                                token               VARCHAR(512) NOT NULL UNIQUE,
                                expires_at          TIMESTAMPTZ NOT NULL,
                                is_revoked          BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                CONSTRAINT fk_refresh_tokens_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
                                        ON DELETE CASCADE
);

-- ============================================================================
-- INDEXES
-- ============================================================================

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_deleted_at
    ON users(deleted_at);

CREATE INDEX idx_users_last_login
    ON users(last_login_at);

-- Performance Optimization: Speeds up role authorization lookups inside Spring Security filters
CREATE INDEX idx_user_roles_name
    ON user_roles(role_name);

CREATE INDEX idx_refresh_tokens_user
    ON refresh_tokens(user_id);

CREATE INDEX idx_refresh_tokens_token
    ON refresh_tokens(token);

CREATE INDEX idx_refresh_tokens_expires_at
    ON refresh_tokens(expires_at);
