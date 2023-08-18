package com.example.entity;

import com.example.enums.VideoType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity extends BaseStringEntity{
    @Column(name = "preview_attach_id")
    private String  previewAttachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_attach_id",insertable = false,updatable = false)
    private AttachEntity previewAttach;
    @Column(name = "title")
    private String title;
    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",insertable = false, updatable = false)
    private CategoryEntity category;
    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",insertable = false,updatable = false)
    private AttachEntity attach;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column
    private AccessLevel status;
    @Column
    private VideoType type;
    @Column(name = "view_count")
    private Integer viewCount=0;
    @Column(name = "shared_count")
    private Integer sharedCount=0;
    @Column
    private String description;
    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",insertable = false,updatable = false)
    private ChannelEntity channel;
    @Column(name = "like_count")
    private Integer likeCount=0;
    @Column(name = "dislike_count")
    private Integer dislikeCount=0;
//    view_count -> Okala view_count buyerda ham bo'lsin. Alohida Table ham bo'lsin.
//            category_id -> bitta video bitta category bo'lsin.
}
