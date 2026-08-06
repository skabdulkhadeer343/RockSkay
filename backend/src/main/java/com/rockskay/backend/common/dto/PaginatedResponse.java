package com.rockskay.backend.common.dto;

import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;


public record PaginatedResponse<T> (
    boolean success,
    String message,
    List<T> data,
    Instant timestamp,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
){

    private PaginatedResponse(Page<T> pageData, List<T> data) {

        this(true, "Data fetched successfully", data, Instant.now(), pageData.getNumber(), pageData.getSize(), pageData.getTotalElements(), pageData.getTotalPages(), pageData.isFirst(), pageData.isLast());

    }


    public static <T> PaginatedResponse<T> of(
            Page<T> page,
            List<T> data
    ) {
        return new PaginatedResponse<>(page, data);
    }
}