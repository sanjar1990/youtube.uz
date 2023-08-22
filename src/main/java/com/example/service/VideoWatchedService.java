package com.example.service;

import com.example.entity.VideoWatchedEntity;
import com.example.repository.VideoWatchedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoWatchedService {
    @Autowired
    private VideoWatchedRepository videoWatchedRepository;
    public void create(String videoId, Integer prtId){
        Optional<VideoWatchedEntity>optional=videoWatchedRepository
                .findByVideoIdAndProfileIdAndVisibleTrue(videoId,prtId);
        if(optional.isPresent()) return;
        VideoWatchedEntity entity=new VideoWatchedEntity();
        entity.setVideoId(videoId);
        entity.setProfileId(prtId);
        videoWatchedRepository.save(entity);
    }

}
