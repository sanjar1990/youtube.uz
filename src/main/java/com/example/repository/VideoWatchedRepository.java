package com.example.repository;

import com.example.entity.VideoWatchedEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VideoWatchedRepository extends CrudRepository<VideoWatchedEntity,String> {
    Optional<VideoWatchedEntity>findByVideoIdAndProfileIdAndVisibleTrue(String videoId,Integer profileId);
}
