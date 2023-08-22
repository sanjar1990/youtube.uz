package com.example.mapper;

import java.time.LocalDateTime;

public interface VideoFullInfoMapperI {
    String getVideoId();
    String getVideoTitle();
    String getVideoDescription();
    String getPreviewAttachId();
    String getAttachId();
    Long getVideoDuration();
    Integer getCategoryId();
    String getCategoryName();
    String getTagId();
    String getTagName();
    LocalDateTime getPublishedDate();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    Integer getViewCount();
    Integer getSharedCount();
    Integer getLikeCount();
    Integer getDislikeCount();
}
