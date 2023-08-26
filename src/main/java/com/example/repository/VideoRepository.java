package com.example.repository;

import com.example.entity.VideoEntity;
import com.example.mapper.VideoFullInfoMapperI;
import com.example.mapper.VideoPlaylistInfoMapperI;
import com.example.mapper.VideoShortInfoMapperI;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends CrudRepository<VideoEntity,String > {
    Optional<VideoEntity>findByIdAndProfileIdAndVisibleTrue(String videoId,Integer prtId);
    Optional<VideoEntity>findByIdAndVisibleTrue(String videoId);
    @Query("select v.id as videoId, v.title as videoTitle, v.previewAttachId as previewAttachId," +
            " v.publishedDate as publishDate, v.channelId as channelId, ch.name as channelName," +
            " ch.photoId as channelPhotoId, v.viewCount as viewCount, a.duration as videoDuration " +
            "from VideoEntity as v inner join v.channel as ch inner join v.attach as a " +
            "where v.categoryId=:categoryId and v.status=PUBLIC and v.visible=true ")
    Page<VideoShortInfoMapperI> getByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);
    @Query("select v.id as videoId, v.title as videoTitle, v.previewAttachId as previewAttachId," +
            " v.publishedDate as publishDate, v.channelId as channelId, ch.name as channelName," +
            " ch.photoId as channelPhotoId, v.viewCount as viewCount, a.duration as videoDuration " +
            "from VideoEntity as v inner join v.channel as ch inner join v.attach as a " +
            "where v.status=PUBLIC and v.visible=true and v.title like :title ")
    List<VideoShortInfoMapperI>getByTitle(String title);

    @Query("select v.id as videoId, v.title as videoTitle, v.previewAttachId as previewAttachId," +
            " v.publishedDate as publishDate, v.channelId as channelId, ch.name as channelName," +
            " ch.photoId as channelPhotoId, v.viewCount as viewCount, a.duration as videoDuration " +
            "from VideoTagEntity as vt  inner join vt.video as v inner join v.channel as ch inner join v.attach as a " +
            "where v.status=PUBLIC and v.visible=true and vt.tagId=:tagId ")
    Page<VideoShortInfoMapperI>getByTagId(@Param("tagId")String tagId,Pageable pageable);
    @Query(value = "select v.id as videoId, v.title as videoTitle, v.description as videoDescription," +
            " v.preview_attach_id as previewAttachId, a.id as attachId, a.duration as videoDuration," +
            " c.id as category_id, c.name as category_name, v.published_date as publishedDate," +
            "  ch.id as channelId, ch.name as channelName, ch.photo_id as channelPhotoId, v.view_count as viewCount," +
            " v.shared_count as sharedCount, v.like_count as likeCount, v.dislike_count as dislikeCount" +
            " from video as v inner join attach as a " +
            "on v.attach_id=a.id inner join category as c on v.category_id=c.id" +
            " inner join channel as ch on v.channel_id=ch.id " +
            "where v.id=:videoId and v.visible=true", nativeQuery = true)
    VideoFullInfoMapperI getById(@Param("videoId") String videoId);
    @Transactional
    @Modifying
    @Query("update VideoEntity set viewCount=viewCount+1 where id=:videoId")
    void increaseViewCount(@Param("videoId") String videoId);

    // TODO: 8/20/2023 playlist yozish
    @Query("select v.id as videoId, v.title as videoTitle, v.previewAttachId as previewAttachId," +
            " v.publishedDate as publishDate, v.channelId as channelId, ch.name as channelName," +
            " ch.photoId as channelPhotoId, v.viewCount as viewCount, a.duration as videoDuration," +
            " p.id as profileId, p.name as profileName, p.surname as profileSurname from VideoEntity as v inner join v.channel as ch inner join v.attach as a " +
            "inner join v.profile as p " +
            "where v.visible=true ")
    Page<VideoShortInfoMapperI>getVideoList(Pageable pageable);

    @Query("select v.id as videoId, v.title as videoTitle, v.previewAttachId as previewAttachId, v.viewCount as viewCount," +
            " v.publishedDate as publishedDate, a.id as attachId, a.duration as duration from VideoEntity as v " +
            "inner join v.attach as a where v.channelId=:channelId and v.status in (:list) and v.visible=true")
    Page<VideoPlaylistInfoMapperI>getByChannelId(@Param("channelId") String channelId,
                                                 @Param("list") List<AccessLevel> statusList,Pageable pageable );

    Boolean existsByIdAndStatusAndVisibleTrue(String videoId,AccessLevel status);
}
