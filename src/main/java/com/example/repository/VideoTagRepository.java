package com.example.repository;

import com.example.entity.TagEntity;
import com.example.entity.VideoTagEntity;
import com.example.mapper.VideoTagMapper;
import com.example.mapper.VideoTagMapperI;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity,String> {
    @Query("select count(vt) from VideoTagEntity as vt inner join vt.video.channel as ch " +
            "where vt.videoId=:videoId and ch.profileId=:prtId")
  Long getVideoTag(@Param("videoId") String videoId, @Param("prtId") Integer prtId);
    @Transactional
    @Modifying
    int deleteByVideoIdAndTagId(String videoId, String tagId);
    @Query("select vt.id  as id, vt.videoId as videoId, t.id as tagId, t.name as tagName, vt.createdDate as createdDate from VideoTagEntity as vt inner join vt.tag as t" +
            " where vt.videoId=:videoId")
    List<VideoTagMapperI>getAll(@Param("videoId") String videoId);
    List<VideoTagEntity>findAllByVideoIdAndVisibleTrue(String videoId);
  @Query("select t from VideoTagEntity as vt inner join vt.tag as t where vt.videoId=:videoId")
  List<TagEntity> findByVideoId(@Param("videoId") String videoId);
}
