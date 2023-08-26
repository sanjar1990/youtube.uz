package com.example.mapper;

import lombok.AccessLevel;

import java.time.LocalDateTime;

public interface PlaylistShortInfoMapperI {
    String getPlaylistId();
    String getPlaylistName();
    LocalDateTime getCreatedDate();
    String getChannelId();
    String getChannelName();
    Long getVideoCount();

    String getVideoId();
    String getVideoName();
    Long getDuration();
}
