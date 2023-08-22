package com.example.mapper;

import com.example.dto.TagDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoTagMapper {
    private String id;
    private String videoId;
    private TagDTO tagDTO;
    private LocalDateTime createdDate;
}
