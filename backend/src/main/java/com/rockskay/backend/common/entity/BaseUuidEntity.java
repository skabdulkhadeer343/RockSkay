package com.rockskay.backend.common.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class BaseUuidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)    
    private UUID id;

}