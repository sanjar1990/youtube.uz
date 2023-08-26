package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDTO {
    @NotBlank(message = "Content is required")
    private String content;
}
