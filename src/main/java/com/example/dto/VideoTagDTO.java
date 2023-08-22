package com.example.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoTagDTO {
    @NotBlank(message = "video id is required")
    private String videoId;
    @NotBlank(message = "video id is required")
    private String tagId;
}
