package com.example.repository;

import com.example.config.OpenAPIConfig;
import com.example.entity.PlaylistEntity;
import com.example.entity.PlaylistVideoEntity;
import com.example.mapper.PlaylistMapperI;
import com.example.mapper.PlaylistShortInfoMapperI;
import com.example.mapper.PlaylistVideoInfoMapperI;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity,String> {
    Optional<PlaylistVideoEntity> findByPlaylistIdAndOrderNumAndVisibleTrue(String playlistId,Integer orderNum);

   @Query("select v.id as videoId, v.title as videoName, a.duration as duration from PlaylistVideoEntity as pv inner join pv.video as v" +
           " inner join v.attach as a where pv.playlistId=:plyId and pv.visible=true ")
    List<PlaylistShortInfoMapperI> getByPlaylistId(@Param("plyId") String plyId);
   Boolean existsByPlaylistIdAndVideoIdAndVisibleTrue(String plyId,String videoId);

   @Query(value = " select p.id as playlistId, p.name as playlistName, count(pv.video_id) as videoCount," +
           " sum(v.view_count) as totalViewCount, (select pv.created_date from playlist_video as pv inner join playlist as p" +
           " on pv.playlist_id=p.id where p.id=:plyId order by pv.created_date desc limit 1)as lastUpdateDate" +
           " from playlist_video as pv" +
           " inner join playlist as p on pv.playlist_id=p.id inner join video as v on pv.video_id=v.id" +
           " where p.id=:plyId" +
           " group by p.id, p.name" ,nativeQuery = true)
    PlaylistMapperI getPlaylistById(@Param("plyId") String playlistId);
    @Query("from PlaylistVideoEntity as pv inner join pv.playlist as p inner join p.channel as ch" +
            " where pv.id=:pvId and ch.profileId=:profileId")
    PlaylistVideoEntity get(@Param("profileId") Integer id, @Param("pvId") String pvId);

   @Transactional
   @Modifying
   @Query("update PlaylistVideoEntity set visible=false  where playlistId=:plyId and videoId=:videoId")
    int deletePV(@Param("plyId") String playlistId, @Param("videoId") String videoId);

    @Query("select pv.playlistId as playlistId, v.id as videoId, pa.id as previewAttachId, v.title as videoTitle," +
            " a.duration as duration, ch.id as channelId, ch.name as channelName, pv.createdDate as createdDate, " +
            "pv.orderNum as orderNum from PlaylistVideoEntity as pv " +
            " inner join pv.video as v inner join v.previewAttach as pa inner join v.attach as a inner join pv.playlist as p" +
            " inner join p.channel as ch where p.id=:plyId and pv.visible=true" +
            " and v.status=PUBLIC order by pv.orderNum desc ")
    List<PlaylistVideoInfoMapperI> getPlaylistInfo(@Param("plyId") String plyId);
}
