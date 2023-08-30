package com.example.mapper;

import com.example.enums.NotificationType;

import java.time.LocalDateTime;

public interface SubInfoMapperI {
    String getSubId();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    NotificationType getNotificationType();
    LocalDateTime getCreatedDate();
}
