package com.rockskay.backend.common.exception.resource;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{


    public ResourceNotFoundException(String message) {
        super(message);
    }
}
