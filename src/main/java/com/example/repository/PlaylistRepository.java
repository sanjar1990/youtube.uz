package com.example.repository;

import com.example.entity.PlaylistEntity;
import com.example.mapper.PlaylistInfoMapperI;
import com.example.mapper.PlaylistShortInfoMapperI;
import com.example.mapper.PlaylistVideoInfoMapperI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<PlaylistEntity,String>,
        PagingAndSortingRepository<PlaylistEntity,String> {
    Optional<PlaylistEntity>findByChannelIdAndOrderNumAndVisibleTrue(String channelId, Integer orderNum);
    @Query("from PlaylistEntity as p inner join p.channel as ch" +
            " where p.id=:plId and  ch.profileId=:prtId and p.visible=true")
   PlaylistEntity findByIdUser(@Param("prtId") Integer id, @Param("plId") String plyId);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible=false where id=:plyId")
    int deletePlaylist(@Param("plyId") String playlistId);
    @Query("select p.id as playlistId, p.name as playlistName, p.description as description, " +
            " p.status as status, p.orderNum as orderNum, ch.id as channelId, ch.name as channelName," +
            "ch.photoId as channelPhotoId, pr.id as profileId, pr.name as profileName, pr.surname as surname," +
            "pr.attachId as profilePhotoId from PlaylistEntity as p inner join p.channel as ch inner  join ch.profile as pr" +
            " where p.visible=true")
      Page<PlaylistInfoMapperI>pagination(Pageable pageable);

    @Query("select p.id as playlistId, p.name as playlistName, p.description as description, " +
            " p.status as status, p.orderNum as orderNum, ch.id as channelId, ch.name as channelName," +
            "ch.photoId as channelPhotoId, pr.id as profileId, pr.name as profileName, pr.surname as surname," +
            "pr.attachId as profilePhotoId from PlaylistEntity as p inner join p.channel as ch inner  join ch.profile as pr" +
            " where p.visible=true and pr.id=:userId")
    Page<PlaylistInfoMapperI> paginationByUser(Pageable pageable,@Param("userId") Integer userId);

   @Query(value = "select p.id as playlistId, p.name as playlistName, p.createdDate as createdDate," +
           " ch.id as channelId, ch.name as channelName from PlaylistEntity as p " +
           "inner  join p.channel as ch" +
           " where ch.profileId=:prtId and p.visible=true order by p.orderNum desc " )
    List<PlaylistShortInfoMapperI> getUserPlaylist(@Param("prtId") Integer id);
    @Query(value = "select p.id as playlistId, p.name as playlistName, p.createdDate as createdDate," +
            " ch.id as channelId, ch.name as channelName from PlaylistEntity as p " +
            "inner  join p.channel as ch" +
            " where ch.id=:channelId and p.status=PUBLIC and p.visible=true order by p.orderNum desc " )
    List<PlaylistShortInfoMapperI> getByChannel(@Param("channelId") String channelId);


}
