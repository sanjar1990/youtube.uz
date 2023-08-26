package com.example.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistMapper {
    private String playlistId;
    private String playlistName;
    private Long videoCount;
    private Long totalViewCount;
    private LocalDateTime lastUpdateDate;

    public PlaylistMapper(String playlistId, String playlistName, Long videoCount,
                          Long totalViewCount, LocalDateTime lastUpdateDate) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.videoCount = videoCount;
        this.totalViewCount = totalViewCount;
        this.lastUpdateDate = lastUpdateDate;
    }
}
