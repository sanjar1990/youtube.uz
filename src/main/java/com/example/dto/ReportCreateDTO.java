package com.example.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCreateDTO {
    @NotBlank(message = "content is required")
    private String content;
    private String channelId;
    private String videoId;
}
