package com.example.service;

import com.example.dto.*;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.VideoEntity;
import com.example.enums.Language;
import com.example.exception.AppMethodNotAllowedException;
import com.example.mapper.CommentInfoMapperI;
import com.example.mapper.CommentPaginationMapperI;
import com.example.repository.CommentRepository;
import com.example.repository.VideoRepository;
import com.example.util.SpringSecurityUtil;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private VideoRepository videoRepository;
// 1. Crate Comment (USER)
    public CommentDTO create(CommentCreateDTO dto) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        CommentEntity entity= new CommentEntity();
        entity.setProfileId(profile.getId());
        entity.setContent(dto.getContent());
        entity.setVideoId(dto.getVideoId());
        entity.setReplyId(dto.getReplyId());
        Boolean result=videoRepository.existsByIdAndStatusAndVisibleTrue(dto.getVideoId(), AccessLevel.PUBLIC);
        if(!result) throw new AppMethodNotAllowedException(resourceBundleService
                .getMessage("method.not.allowed", profile.getLanguage()));
        commentRepository.save(entity);
        return toDto(entity);
    }
//2. Update Comment (USER AND OWNER)
    public CommentDTO update(String commentId, CommentUpdateDTO dto) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        CommentEntity entity=get(commentId, profile.getId(), profile.getLanguage());
        entity.setContent(dto.getContent());
        commentRepository.save(entity);
        return toDto(entity);
    }

   // 3. Delete Comment (USER AND OWNER, ADMIN)
    public ApiResponseDTO delete(String commentId) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        CommentEntity comment=get(commentId, profile.getId(), profile.getLanguage());
        int m=commentRepository.deleteReplyComment(comment.getId());
        int n =commentRepository.deleteComment(commentId,profile.getId());
        return n>0&m>0?new ApiResponseDTO(false,"deleted")
                :new ApiResponseDTO(true,"comment not deleted");
    }
    private CommentEntity get(String commentId, Integer prtId, Language language) {
        return commentRepository.findByIdAndProfileIdAndVisibleTrue(commentId, prtId)
                .orElseThrow(() -> {
                    throw new AppMethodNotAllowedException(resourceBundleService
                            .getMessage("method.not.allowed", language));
                });
    }
//4. Comment List Pagination (ADMIN)
    public PageImpl<CommentDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<CommentEntity> pageObj=commentRepository.findByVisibleTrue(pageable);
        List<CommentDTO>dtoList=pageObj.getContent().stream().map(s->toDto(s)).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    // 5. Comment List By profileId(ADMIN)
    public PageImpl<CommentDTO> paginationByProfileId(Integer prtId, Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<CommentPaginationMapperI>pageObj=commentRepository.paginationByPrt(prtId,pageable);
        List<CommentDTO> dtoList =getCommentList(pageObj.getContent());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    //6. Comment List By Profile (murojat qilgan odamning comment lari) (USER AND OWNER)
    public List<CommentDTO> commentList() {
        return getCommentList(commentRepository.commentList(SpringSecurityUtil.getProfileId()));
    }
    //7. Comment List by videoId
    public List<CommentDTO> byVideoId(String videoId) {
        return getCommentInfo(commentRepository.byVideoId(videoId));
    }
    // 8. Get Comment Replied Comment by comment Id (Commentga yozilgan commentlar)
    public List<CommentDTO> getReply(String commentId) {
        return getCommentInfo(commentRepository.getReply(commentId));
    }
    private List<CommentDTO>getCommentInfo(List<CommentInfoMapperI> mapper){
       return mapper.stream().map(s->{
            CommentDTO commentDTO= new CommentDTO();
            commentDTO.setId(s.getCommentId());
            commentDTO.setContent(s.getContent());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setLikeCount(s.getLikeCount());
            commentDTO.setDislikeCount(s.getDislikeCount());
            ProfileDTO profileDTO= new ProfileDTO();
            profileDTO.setId(s.getProfileId());
            profileDTO.setName(s.getName());
            profileDTO.setSurname(s.getSurname());
            AttachDTO attachDTO= new AttachDTO();
            attachDTO.setId(s.getPhotoId());
            attachDTO.setUrl(attachService.getUrl(s.getPhotoId()));
            profileDTO.setAttach(attachDTO);
            commentDTO.setProfile(profileDTO);
            return commentDTO;
        }).toList();
    }
    private CommentDTO toDto(CommentEntity entity){
        CommentDTO commentDTO= new CommentDTO();
        commentDTO.setDislikeCount(entity.getDislikeCount());
        commentDTO.setLikeCount(entity.getLikeCount());
        commentDTO.setReplyId(entity.getReplyId());
        commentDTO.setVideoId(entity.getVideoId());
        commentDTO.setContent(entity.getContent());
        commentDTO.setProfileId(entity.getProfileId());
        return commentDTO;
    }

    private List<CommentDTO>getCommentList(List<CommentPaginationMapperI>mapper){
        return mapper.stream().map(s->{
            CommentDTO commentDTO= new CommentDTO();
            commentDTO.setId(s.getCommentId());
            commentDTO.setContent(s.getContent());
            commentDTO.setCreatedDate(s.getCreatedDate());
            commentDTO.setLikeCount(s.getLikeCount());
            commentDTO.setDislikeCount(s.getDislikeCount());
            VideoDTO videoDTO= new VideoDTO();
            videoDTO.setId(s.getVideoId());
            videoDTO.setTitle(s.getTitle());
            AttachDTO preview=new AttachDTO();
            preview.setId(s.getPreviewAttachId());
            preview.setUrl(attachService.getUrl(s.getPreviewAttachId()));
            videoDTO.setPreviewAttach(preview);
            AttachDTO attachDTO= new AttachDTO();
            attachDTO.setId(s.getAttachId());
            attachDTO.setDuration(s.getDuration());
            videoDTO.setAttach(attachDTO);
            commentDTO.setVideo(videoDTO);
            return commentDTO;
        }).toList();
    }
}
