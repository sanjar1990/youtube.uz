package com.example.dto;

import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.LikeDislikeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeDTO {
    private String id;
    private Integer profileId;
    private String commentId;
    private LikeDislikeType type;
    private LocalDateTime createdDate;
}
