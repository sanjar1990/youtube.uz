package com.example.repository;

import com.example.entity.ReportEntity;
import com.example.mapper.ReportInfoMapperI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReportRepository extends CrudRepository<ReportEntity,String>,
        PagingAndSortingRepository<ReportEntity,String> {
   @Query("select r.id as id, p.id as profileId, p.name as profileName, p.surname as profileSurname," +
           "p.attachId as photoId, r.content as content, r.channelId as channelId, r.videoId as videoId, " +
           "r.type as type, r.createdDate as createdDate from ReportEntity as r inner join r.profile as p" +
           " where r.visible=true order by r.createdDate desc ")
    Page<ReportInfoMapperI>pagination(Pageable pageable);
 @Query("select r.id as id, p.id as profileId, p.name as profileName, p.surname as profileSurname," +
         "p.attachId as profilePhotoId, r.content as content, r.channelId as channelId, r.videoId as videoId, " +
         "r.type as type, r.createdDate as createdDate from ReportEntity as r inner join r.profile as p" +
         " where p.id=:userId and r.visible=true order by r.createdDate desc ")
 List<ReportInfoMapperI> reportList(@Param("userId")Integer userId);

    @Transactional
    @Modifying
    @Query("update ReportEntity set visible=false where id=:id")
    int deleteReport(@Param("id") String reportId);
}
