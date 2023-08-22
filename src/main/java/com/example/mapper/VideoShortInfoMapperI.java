package com.example.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfoMapperI {
    String getVideoId();
    String getVideoTitle();
    String getPreviewAttachId();
    LocalDateTime getPublishedDate();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    Integer getViewCount();
    Long getVideoDuration();
    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String getPlaylistId();
    String getPlaylistName();
}
