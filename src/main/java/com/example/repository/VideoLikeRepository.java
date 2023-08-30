package com.example.repository;

import com.example.entity.VideoLikeEntity;
import com.example.mapper.VideoLikeInfoMapperI;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity,String> {
    Optional<VideoLikeEntity>findByProfileIdAndVideoId(Integer prtId, String videoId);
    @Query("select vl.id as videoLikeId, v.id as videoId, v.title as videoName, ch.id as channelId," +
            " ch.name as channelName, a.id as attachId, a.duration as attachDuration, " +
            "v.previewAttachId as previewAttachId from VideoLikeEntity as vl inner join vl.video as v" +
            " inner join v.channel as ch inner join v.attach as a " +
            " where vl.profileId=:prtId and vl.type=LIKE " +
            " order by vl.createdDate desc ")
    List<VideoLikeInfoMapperI>likedVideoInfo(@Param("prtId") Integer prtId);
}
