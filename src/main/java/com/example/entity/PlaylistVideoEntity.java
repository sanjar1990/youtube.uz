package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "playlist_video")
public class PlaylistVideoEntity extends BaseStringEntity {
    @Column(name = "playlist_id")
    private String playlistId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id",insertable = false,updatable = false)
    private PlaylistEntity playlist;
    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable = false,updatable = false)
    private VideoEntity video;
    @Column(name = "order_num")
    private Integer orderNum;
}
