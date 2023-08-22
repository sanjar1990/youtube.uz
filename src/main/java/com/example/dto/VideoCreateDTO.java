package com.example.dto;

import com.example.enums.VideoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VideoCreateDTO {
    private String  previewAttachId;
    @NotBlank(message = "title is required")
    private String title;
    @NotNull(message = "category is required")
    private Integer categoryId;
    @NotBlank(message = "Attach is required")
    private String attachId;
    private LocalDateTime publishedDate;
    @NotNull(message = "status is required")
    private AccessLevel status;
    @NotBlank(message = "description is required")
    private String description;
    @NotNull(message = "channel id is required")
    private String channelId;
    private List<String> tagDTOList;
}
