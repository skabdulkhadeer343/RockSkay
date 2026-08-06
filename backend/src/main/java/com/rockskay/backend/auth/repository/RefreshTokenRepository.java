package com.rockskay.backend.auth.repository;

import com.rockskay.backend.auth.entity.RefreshToken;
import org.aspectj.weaver.loadtime.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {


    public Optional<RefreshToken> findByToken(String token);
}
