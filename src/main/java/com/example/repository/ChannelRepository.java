package com.example.repository;

import com.example.entity.ChannelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends CrudRepository<ChannelEntity,String>,
        PagingAndSortingRepository<ChannelEntity,String> {
    Optional<ChannelEntity>findByIdAndVisibleTrue(String channelId);
    List<ChannelEntity>findAllByProfileIdAndVisibleTrue(Integer prtId);

}
