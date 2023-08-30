package com.example.entity;

import com.example.dto.ChannelDTO;
import com.example.enums.ActiveBlockStatus;
import com.example.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class SubscriptionEntity extends BaseStringEntity{
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;
    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",insertable = false,updatable = false)
    private ChannelEntity channel;
    @Column(name = "unsubscribe_date")
    private LocalDateTime unsubscribeDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveBlockStatus status=ActiveBlockStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType=NotificationType.PERSONALIZED;
}
