package com.example.entity;

import com.example.dto.CommentDTO;
import com.example.enums.LikeDislikeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment_like")
public class CommentLikeEntity extends BaseStringEntity {
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "comment_id")
    private String commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id",insertable = false,updatable = false)
    private CommentEntity comment;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LikeDislikeType type;
}
