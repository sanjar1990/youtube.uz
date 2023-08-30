package com.example.repository;

import com.example.entity.SubscriptionEntity;
import com.example.mapper.SubInfoMapperI;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity,String > {
    Optional<SubscriptionEntity>findByProfileIdAndChannelId(Integer profileId, String channelId);

   @Query("select s.id as subId, ch.id as channelId, ch.name as channelName, ch.photoId as channelPhotoId," +
           " s.notificationType as notificationType, s.createdDate as createdDate from SubscriptionEntity as s inner join s.channel as ch " +
           " where s.visible=true and s.profileId=:prtId and s.status=ACTIVE" +
           " order by s.createdDate desc ")
    List<SubInfoMapperI>getSubList(@Param("prtId") Integer prtId);

}
