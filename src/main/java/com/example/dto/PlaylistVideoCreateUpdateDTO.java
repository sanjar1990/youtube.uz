package com.example.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistVideoCreateUpdateDTO {
    @NotBlank(message = "playlist id is required")
    private String playlistId;
    @NotBlank(message = "video id is required")
    private String videoId;
    @NotNull(message = "order number is required")
    private Integer orderNum;
}
