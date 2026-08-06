package com.rockskay.backend.common.exception.resource;

public class ResourceAlreadyVerifiedException extends  RuntimeException{

    public ResourceAlreadyVerifiedException(String message) {
        super(message);
    }

}