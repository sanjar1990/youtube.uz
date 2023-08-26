package com.example.mapper;

import lombok.AccessLevel;

public interface PlaylistInfoMapperI {
    String getPlaylistId();
    String getPlaylistName();
    String getDescription();
    AccessLevel getStatus();
    Integer getOrderNum();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    Integer getProfileId();
    String getProfileName();
    String getSurname();
    String getProfilePhotoId();

}
