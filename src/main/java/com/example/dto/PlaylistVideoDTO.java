package com.example.dto;

import com.example.entity.PlaylistEntity;
import com.example.entity.VideoEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDTO {
    private String id;
    private String playlistId;
    private String videoId;
    private LocalDateTime createdDate;
    private Integer orderNum;
}
