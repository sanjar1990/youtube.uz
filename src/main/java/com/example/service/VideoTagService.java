package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.TagDTO;
import com.example.dto.VideoTagDTO;
import com.example.entity.TagEntity;
import com.example.entity.VideoEntity;
import com.example.entity.VideoTagEntity;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.mapper.VideoTagMapper;
import com.example.mapper.VideoTagMapperI;
import com.example.repository.VideoTagRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideoTagService {
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
// 1. Add tag to video. (USER and OWNER)
    public void create(String videoId,String tagId){
        VideoTagEntity entity=new VideoTagEntity();
        entity.setVideoId(videoId);
        entity.setTagId(tagId);
        videoTagRepository.save(entity);
    }
//   2. Delete tag from vide (USER and OWNER)
    public ApiResponseDTO delete(VideoTagDTO videoTagDTO, Language language) {
        Integer prtId= SpringSecurityUtil.getProfileId();
        Long m =videoTagRepository.getVideoTag(videoTagDTO.getVideoId(),prtId);
        System.out.println(m);
        if(m>0){
            System.out.println("exception");
            throw new AppBadRequestException(resourceBundleService.getMessage("method.not.allowed", language));
        }
        int n=videoTagRepository.deleteByVideoIdAndTagId(videoTagDTO.getVideoId(), videoTagDTO.getTagId());
        return n>0? new ApiResponseDTO(false,"video deleted"):new ApiResponseDTO(true,"video not deleted");
    }

    public List<VideoTagMapper> getAllList(String videoId) {
        List<VideoTagMapperI> mapperList=videoTagRepository.getAll(videoId);
        return mapperList.stream().map(s->{
            VideoTagMapper mapper=new VideoTagMapper();
            mapper.setId(s.getId());
            mapper.setVideoId(s.getVideoId());
            TagDTO tagDTO= new TagDTO();
            tagDTO.setId(s.getTagId());
            tagDTO.setName(s.getTagName());
            mapper.setTagDTO(tagDTO);
            mapper.setCreatedDate(s.getCreatedDate());
            return mapper;
        }).collect(Collectors.toList());
    }

    public void update(String videoId, String tagId) {
        List<VideoTagEntity> list=videoTagRepository.findAllByVideoIdAndVisibleTrue(videoId);
        for (VideoTagEntity v:list) {
            if(!v.getTagId().equals(tagId)){
                create(videoId,tagId);
            }
        }
    }

    public List<TagDTO>getByVideoId(String videoId){
        List<TagEntity> entityList=videoTagRepository.findByVideoId(videoId);
        return entityList.stream().map(s->{
            TagDTO tagDTO = new TagDTO(s.getName(),s.getId());
            return tagDTO;
        }).toList();
    }
}
