package com.example.dto;

import com.example.entity.AttachEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ChannelEntity;
import com.example.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {
    private String  previewAttachId;
    private String title;
    private Integer categoryId;
    private String attachId;
    private LocalDateTime publishedDate;
    private AccessLevel status;
    private VideoType type;
    private Integer viewCount=0;
    private Integer sharedCount=0;
    private String description;
    private String channelId;
    private Integer likeCount=0;
    private Integer dislikeCount=0;
}
