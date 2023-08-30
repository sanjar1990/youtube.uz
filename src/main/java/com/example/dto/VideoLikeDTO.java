package com.example.dto;

import com.example.entity.ProfileEntity;
import com.example.entity.VideoEntity;
import com.example.enums.LikeDislikeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoLikeDTO {
    private String id;
    private Integer profileId;
    private String videoId;
    private LikeDislikeType type;
    private VideoDTO video;
}
