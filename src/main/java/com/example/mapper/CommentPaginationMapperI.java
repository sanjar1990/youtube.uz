package com.example.mapper;

import java.time.LocalDateTime;

public interface CommentPaginationMapperI {
    String getCommentId();
    String getContent();
    LocalDateTime getCreatedDate();
    Long getLikeCount();
    Long getDislikeCount();
    String getVideoId();
    String getTitle();
    String getPreviewAttachId();
    String getAttachId();
    Long getDuration();
}
