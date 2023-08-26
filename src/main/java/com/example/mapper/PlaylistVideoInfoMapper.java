package com.example.mapper;

import com.example.dto.ChannelDTO;
import com.example.dto.VideoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoInfoMapper {
    private String playlistId;
    private VideoDTO video;
    private ChannelDTO channel;
    private LocalDateTime createdDate;
    private Integer orderNum;
}
