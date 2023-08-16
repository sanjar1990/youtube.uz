package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDTO {
    private Boolean isError;
    private String message;
    private Object data;

    public ApiResponseDTO(Boolean isError, String message) {
        this.isError = isError;
        this.message = message;
    }

    public ApiResponseDTO(Boolean isError, Object data) {
        this.isError = isError;
        this.data = data;
    }
}
