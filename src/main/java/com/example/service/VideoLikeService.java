package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AttachDTO;
import com.example.dto.VideoDTO;
import com.example.dto.VideoLikeDTO;
import com.example.entity.ProfileEntity;
import com.example.entity.VideoLikeEntity;
import com.example.enums.LikeDislikeType;
import com.example.mapper.VideoLikeInfoMapperI;
import com.example.repository.VideoLikeRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoLikeService {
    @Autowired
    private VideoLikeRepository videoLikeRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;

//     1. Create Video like
    public ApiResponseDTO like(String videoId) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        VideoLikeEntity entity;
        assert profile != null;
        Optional<VideoLikeEntity> optional=videoLikeRepository.findByProfileIdAndVideoId(profile.getId(), videoId);
        if(optional.isPresent()){
             entity=optional.get();
            if(entity.getType().equals(LikeDislikeType.LIKE)){
            remove(entity);
            return new ApiResponseDTO(false,resourceBundleService.getMessage("like.removed",profile.getLanguage()));
            }else {
                entity.setType(LikeDislikeType.LIKE);
            }
        }else {
            entity=new VideoLikeEntity();
            entity.setVideoId(videoId);
            entity.setProfileId(profile.getId());
            entity.setType(LikeDislikeType.LIKE);
        }
        videoLikeRepository.save(entity);
        return new ApiResponseDTO(false,toDto(entity));
    }
    //     1. Create Video like
    public ApiResponseDTO dislike(String videoId) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        VideoLikeEntity entity;
        assert profile != null;
        Optional<VideoLikeEntity> optional=videoLikeRepository.findByProfileIdAndVideoId(profile.getId(), videoId);
        if(optional.isPresent()){
            entity=optional.get();
            if(entity.getType().equals(LikeDislikeType.DISLIKE)){
            remove(entity);
            return new ApiResponseDTO(false,resourceBundleService.getMessage("dislike.removed",profile.getLanguage()));
            }else {
                entity.setType(LikeDislikeType.DISLIKE);
            }
        }else {
            entity=new VideoLikeEntity();
            entity.setProfileId(profile.getId());
            entity.setVideoId(videoId);
            entity.setType(LikeDislikeType.DISLIKE);
        }
        videoLikeRepository.save(entity);
        return new ApiResponseDTO(false,toDto(entity));
    }
    //2. Remove Video Like
    private void remove(VideoLikeEntity entity){
        videoLikeRepository.delete(entity);
    }
// 3. User Liked Video List (order by created_date desc) (USER)
//4. Get User LikedVideo List By UserId (ADMIN)
    public List<VideoLikeDTO> getLikedVideo(Integer prtId) {
        List<VideoLikeInfoMapperI>mapper=videoLikeRepository.likedVideoInfo(prtId);
        return mapper.stream().map(s->{
            VideoLikeDTO dto= new VideoLikeDTO();
            dto.setId(s.getVideoLikeId());
            VideoDTO videoDTO= new VideoDTO();
            videoDTO.setId(s.getVideoId());
            videoDTO.setTitle(s.getVideoName());
            AttachDTO attachDTO= new AttachDTO();
            attachDTO.setId(s.getVideoAttachId());
            attachDTO.setDuration(s.getAttachDuration());
            videoDTO.setAttach(attachDTO);
            AttachDTO preview=new AttachDTO();
            preview.setId(s.getPreviewAttachId());
            preview.setUrl(attachService.getUrl(s.getPreviewAttachId()));
            videoDTO.setPreviewAttach(preview);
            dto.setVideo(videoDTO);
            return dto;
        }).toList();
    }
    private VideoLikeDTO toDto(VideoLikeEntity entity){
        VideoLikeDTO dto= new VideoLikeDTO();
        dto.setId(entity.getId());
        dto.setVideoId(entity.getVideoId());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }



}
