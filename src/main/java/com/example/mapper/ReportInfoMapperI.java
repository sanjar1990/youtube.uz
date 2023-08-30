package com.example.mapper;

import com.example.enums.ReportType;

import java.time.LocalDateTime;

public interface ReportInfoMapperI {
   String getId();
   Integer getProfileId();
   String getProfileName();
   String getProfileSurname();
   String getProfilePhotoId();
  String getContent();
  String getChannelId();
  String getVideoId();
  ReportType getType();
  LocalDateTime getCreatedDate();
}
