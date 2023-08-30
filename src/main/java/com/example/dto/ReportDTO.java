package com.example.dto;

import com.example.entity.ChannelEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.VideoEntity;
import com.example.enums.ReportType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {
    private String id;
    private Integer profileId;
    private String content;
    private String channelId;
    private String videoId;
    private ReportType type;
    private ProfileDTO profile;
    private LocalDateTime createdDate;
}
