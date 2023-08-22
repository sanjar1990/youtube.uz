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
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {
    private String id;
    private String  previewAttachId;
    private String title;
    private Integer categoryId;
    private String attachId;
    private LocalDateTime publishedDate;
    private AccessLevel status;
    private VideoType type;
    private Integer viewCount;
    private Integer sharedCount;
    private String description;
    private String channelId;
    private Integer likeCount;
    private Integer dislikeCount;
    private ChannelDTO channelDTO;
    private AttachDTO previewAttach;
    private AttachDTO attach;
    private CategoryDTO category;
    private TagDTO tag;
    private List<TagDTO> tagDTOList;
    private Boolean isUserLiked;
    private Boolean isUserDisliked;
    private ProfileDTO profile;
}
