package com.example.mapper;

import java.time.LocalDateTime;

public interface VideoPlaylistInfoMapperI {
    String getVideoId();
    String getVideoTitle();
    String getPreviewAttachId();
    Integer getViewCount();
    LocalDateTime getPublishedDate();
    String getAttachId();
    Long getDuration();
}
