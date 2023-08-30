package com.example.dto;

import com.example.entity.ChannelEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ActiveBlockStatus;
import com.example.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDTO extends BaseStringDTO{
    private Integer profileId;
    private String channelId;
    private LocalDateTime unsubscribeDate;
    private ActiveBlockStatus status;
    private NotificationType notificationType;
    private ChannelDTO channel;
}
