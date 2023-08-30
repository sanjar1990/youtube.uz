package com.example.entity;
import com.example.enums.LikeDislikeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "video_like")
public class VideoLikeEntity extends BaseStringEntity {
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;
    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable = false,updatable = false)
    private VideoEntity video;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LikeDislikeType type;
}
