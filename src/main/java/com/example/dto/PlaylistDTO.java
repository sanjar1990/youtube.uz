package com.example.dto;

import com.example.entity.ChannelEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDTO {
    private String id;
    private String channelId;
    private String name;
    private String description;
    private AccessLevel status;
    private Integer orderNum;
    private ChannelDTO channel;
    private ProfileDTO profile;
    private LocalDateTime createdDate;
    private Integer videoCount;
    private List<VideoDTO>videoDTOList;
}
