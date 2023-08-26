package com.example.mapper;

import java.time.LocalDateTime;

public interface PlaylistMapperI {
    String getPlaylistId();
    String getPlaylistName();
    Long getVideoCount();
    Long getTotalViewCount();
    LocalDateTime getLastUpdateDate();

}
