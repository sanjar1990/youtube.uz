package com.example.mapper;

import java.time.LocalDateTime;

public interface CommentInfoMapperI {
    String getCommentId();
    String getContent();
    LocalDateTime getCreatedDate();
    Long getLikeCount();
    Long getDislikeCount();
    Integer getProfileId();
    String  getName();
    String getSurname();
    String getPhotoId();

}
