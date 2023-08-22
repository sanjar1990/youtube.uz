package com.example.mapper;

import com.example.dto.TagDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public interface VideoTagMapperI {
    String getId();
    String getVideoId();
    String getTagId();
    String getTagName();
    LocalDateTime getCreatedDate();
}
