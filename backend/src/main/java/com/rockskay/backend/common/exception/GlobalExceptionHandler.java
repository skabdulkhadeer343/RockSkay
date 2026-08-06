package com.rockskay.backend.common.exception;

import com.rockskay.backend.common.dto.ErrorResponse;
import com.rockskay.backend.common.exception.otp.InvalidOtpException;
import com.rockskay.backend.common.exception.otp.OtpExpiredException;
import com.rockskay.backend.common.exception.resource.DuplicateResourceException;
import com.rockskay.backend.common.exception.resource.ResourceAlreadyVerifiedException;
import com.rockskay.backend.common.exception.resource.ResourceNotFoundException;
import com.rockskay.backend.common.exception.resource.ResourceNotVerifiedException;
import com.rockskay.backend.common.exception.auth.ForbiddenException;
import com.rockskay.backend.common.exception.auth.InvalidTokenException;
import com.rockskay.backend.common.exception.auth.TokenExpiredException;
import com.rockskay.backend.common.exception.auth.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DuplicateResourceException.class,
            ResourceAlreadyVerifiedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({
            InvalidOtpException.class,
            OtpExpiredException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation failed",
                        errors
                ));
    }

    @ExceptionHandler({
            UnauthorizedException.class,
            InvalidTokenException.class,
            TokenExpiredException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(
                        HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler({
            ForbiddenException.class,
            ResourceNotVerifiedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleForbidden(Exception ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                        HttpStatus.FORBIDDEN.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unexpected error occurred."
                ));
    }
}