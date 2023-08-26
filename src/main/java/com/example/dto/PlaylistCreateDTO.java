package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistCreateDTO {
    @NotBlank(message = "channel id is required")
    private String channelId;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "description is required")
    private String description;
    @NotNull(message = "status is required")
    private AccessLevel status;
    @NotNull(message = "status is required")
    private Integer orderNum;
}
