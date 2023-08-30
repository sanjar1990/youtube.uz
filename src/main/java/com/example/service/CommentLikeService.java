package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CommentLikeDTO;
import com.example.entity.CommentLikeEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.LikeDislikeType;
import com.example.repository.CommentLikeRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
//1. Create Comment like
    public ApiResponseDTO like(String commentId) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        Optional<CommentLikeEntity>optional=commentLikeRepository.findByProfileIdAndCommentId(profile.getId(), commentId);
        CommentLikeEntity entity;
        if(optional.isPresent()){
            entity=optional.get();
        if(entity.getType().equals(LikeDislikeType.LIKE)){
            remove(entity);
            return new ApiResponseDTO(false,resourceBundleService.getMessage("like.removed",profile.getLanguage()));
        }else {
            entity.setType(LikeDislikeType.LIKE);
        }
        }else {
            entity=new CommentLikeEntity();
            entity.setCommentId(commentId);
            entity.setProfileId(profile.getId());
            entity.setType(LikeDislikeType.LIKE);
        }
        commentLikeRepository.save(entity);
    return new ApiResponseDTO(false,toDto(entity));
    }
    //1. Create Comment dislike
    public ApiResponseDTO dislike(String commentId) {
    ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        Optional<CommentLikeEntity>optional=commentLikeRepository.findByProfileIdAndCommentId(profile.getId(), commentId);
    CommentLikeEntity entity;
    if(optional.isPresent()){
        entity= optional.get();
        if(entity.getType().equals(LikeDislikeType.DISLIKE)){
            remove(entity);
        }else {
            entity.setType(LikeDislikeType.DISLIKE);
        }
    }else {
        entity=new CommentLikeEntity();
        entity.setType(LikeDislikeType.DISLIKE);
        entity.setCommentId(commentId);
        entity.setProfileId(profile.getId());
    }
    commentLikeRepository.save(entity);
    return new ApiResponseDTO(false,toDto(entity));
    }
    //2. Remove Comment Like
    private void remove(CommentLikeEntity entity){
        commentLikeRepository.delete(entity);
    }
//  3. User Liked Comment List (order by created_date desc) (USER)
//   4. Get User LikedComment List By UserId (ADMIN)
    public List<CommentLikeDTO> getLikeCommentList(Integer prtId) {
        return toDtoList(commentLikeRepository.findByProfileId(prtId));
    }
    private List<CommentLikeDTO>toDtoList(List<CommentLikeEntity> mapper){
        return mapper.stream().map(s->{
            CommentLikeDTO dto= new CommentLikeDTO();
            dto.setId(s.getId());
            dto.setProfileId(s.getProfileId());
            dto.setCommentId(s.getCommentId());
            dto.setCreatedDate(s.getCreatedDate());
            dto.setType(s.getType());
            return dto;
        }).collect(Collectors.toList());
    }
    private CommentLikeDTO toDto(CommentLikeEntity entity){
        CommentLikeDTO dto= new CommentLikeDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setCommentId(entity.getCommentId());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }




}
