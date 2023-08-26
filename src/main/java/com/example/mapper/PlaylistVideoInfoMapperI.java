package com.example.mapper;

import java.time.LocalDateTime;

public interface PlaylistVideoInfoMapperI {
   String getPlaylistId();
   String getVideoId();
   String getPreviewAttachId();
   String getVideoTitle();
   Long getDuration();
    String getChannelId();
    String  getChannelName();
    LocalDateTime getCreatedDate();
    Integer getOrderNum();
}
