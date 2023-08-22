package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VideoUpdateDTO {
    private String  previewAttachId;
    @NotBlank(message = "title is required")
    private String title;
    @NotNull(message = "category is required")
    private Integer categoryId;
    @NotBlank(message = "description is required")
    private String description;
    private List<String> tagDTOList;
}
