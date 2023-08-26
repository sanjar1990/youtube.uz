package com.example.entity;

import com.example.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "playlist")
public class PlaylistEntity extends BaseStringEntity {
    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",insertable = false,updatable = false)
    private ChannelEntity channel;
    @Column(name = "name",columnDefinition = "TEXT")
    private String name;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column()
    private AccessLevel status;
    @Column(name = "order_num")
    private Integer orderNum;
}
