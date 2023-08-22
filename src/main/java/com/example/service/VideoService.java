package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.enums.Language;
import com.example.enums.VideoType;
import com.example.exception.AppBadRequestException;
import com.example.exception.AppMethodNotAllowedException;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.VideoFullInfoMapperI;
import com.example.mapper.VideoPlaylistInfoMapperI;
import com.example.mapper.VideoShortInfoMapperI;
import com.example.repository.VideoRepository;
import com.example.util.SpringSecurityUtil;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private VideoTagService videoTagService;
    @Autowired
    private VideoWatchedService videoWatchedService;
    @Autowired
    private ChannelService channelService;

    public VideoDTO create(VideoCreateDTO dto, Language language) {
        Integer prtId=SpringSecurityUtil.getProfileId();
        VideoEntity entity=new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setChannelId(dto.getChannelId());
        entity.setProfileId(prtId);
        if(dto.getPublishedDate()==null){
            entity.setPublishedDate(LocalDateTime.now());
        }else {
            entity.setPublishedDate(dto.getPublishedDate());
        }
        AttachEntity attach=attachService.get(dto.getAttachId());
        if(attach.getDuration()>60){
            entity.setType(VideoType.VIDEO);
        }else {
            entity.setType(VideoType.SHORT);
        }
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        videoRepository.save(entity);
        for (String t: dto.getTagDTOList()){
            TagDTO tagDTO=tagService.create(new TagDTO(t));
            videoTagService.create(entity.getId(),tagDTO.getId());
        }
        VideoDTO videoDTO=new VideoDTO();
        videoDTO.setId(entity.getId());
        videoDTO.setTitle(dto.getTitle());
        videoDTO.setDescription(dto.getDescription());
        videoDTO.setAttachId(dto.getAttachId());
        videoDTO.setStatus(dto.getStatus());
        videoDTO.setType(entity.getType());
        videoDTO.setCategoryId(entity.getCategoryId());
        videoDTO.setPreviewAttachId(entity.getPreviewAttachId());
        return videoDTO;
    }
    //2. Update Video Detail (USER and OWNER)
    public ApiResponseDTO update(String videoId, VideoUpdateDTO dto, Language language) {
        Integer prtId= SpringSecurityUtil.getProfileId();
        VideoEntity entity=getByOwner(videoId,prtId,language);
        entity.setDescription(dto.getDescription());
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setTitle(dto.getTitle());
        entity.setCategoryId(dto.getCategoryId());
        for (String t: dto.getTagDTOList()){
            TagDTO tagDTO=tagService.create(new TagDTO(t));
            videoTagService.update(entity.getId(),tagDTO.getId());
        }
        videoRepository.save(entity);
        return new ApiResponseDTO(false,"updated");
    }
    //  3. Change Video Status (USER and OWNER)
    public ApiResponseDTO updateStatus(String videoId, Language language) {
        Integer prtId=SpringSecurityUtil.getProfileId();
        VideoEntity entity=getByOwner(videoId,prtId,language);
        if(entity.getStatus().equals(AccessLevel.PRIVATE)){
            entity.setStatus(AccessLevel.PUBLIC);
        }else {
            entity.setStatus(AccessLevel.PRIVATE);
        }
        videoRepository.save(entity);
        return new ApiResponseDTO(false,"status is updated");
    }
    //   4. Increase video_view Count by id
    public void increaseViewCount(String videoId) {
        Integer prtId=SpringSecurityUtil.getProfileId();
        videoWatchedService.create(videoId,prtId);
    }
    //  5. Get Video Pagination by CategoryId
    public PageImpl<VideoDTO> paginationByCategory(Integer categoryId, Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<VideoShortInfoMapperI>pageObj=videoRepository.getByCategoryId(categoryId,pageable);
        List<VideoDTO>dtoList=toDtoList(pageObj.getContent());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }

    //6. Search video by Title
    public List<VideoDTO> getByTitle(String title) {
        return toDtoList(videoRepository.getByTitle("%"+title+"%"));
    }
    // 7. Get video by tag_id with pagination
    public PageImpl<VideoDTO> getByTag(String tagId,Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<VideoShortInfoMapperI> pageObj=videoRepository.getByTagId(tagId,pageable);
        List<VideoDTO>dtoList=toDtoList(pageObj.getContent());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    private List<VideoDTO> toDtoList(List<VideoShortInfoMapperI> mapper){
        return  mapper.stream().map(s->{
            VideoDTO dto = new VideoDTO();
            dto.setId(s.getVideoId());
            dto.setTitle(s.getVideoTitle());
            AttachDTO previewAttach= new AttachDTO();
            previewAttach.setId(s.getPreviewAttachId());
            previewAttach.setUrl(attachService.getUrl(s.getPreviewAttachId()));
            dto.setPreviewAttach(previewAttach);
            dto.setPublishedDate(s.getPublishedDate());
            ChannelDTO channelDTO= new ChannelDTO();
            channelDTO.setId(s.getChannelId());
            channelDTO.setName(s.getChannelName());
            channelDTO.setPhotoId(s.getChannelPhotoId());
            AttachDTO photo=new AttachDTO();
            photo.setId(s.getChannelPhotoId());
            photo.setUrl(attachService.getUrl(s.getChannelPhotoId()));
            channelDTO.setPhoto(photo);
            dto.setChannelDTO(channelDTO);
            dto.setViewCount(s.getViewCount());
            AttachDTO attach=new AttachDTO();
            attach.setDuration(s.getVideoDuration());
            dto.setAttach(attach);
            return dto;
        }).toList();
    }
    // 8. Get Video By id (If Status PRIVATE allow only for OWNER or ADMIN)
    public VideoDTO getById(String videoId,Language language) {
        videoRepository.increaseViewCount(videoId);
        VideoEntity video=get(videoId,language);
        if(video.getStatus().equals(AccessLevel.PRIVATE)){
            throw new AppMethodNotAllowedException(resourceBundleService.getMessage("method.not.allowed",language));
        }
       return getFullInfo(videoId);
    }

    // 8. Get Video By id (If Status PRIVATE allow only for OWNER or ADMIN)
    public VideoDTO getByIdOwner(String videoId) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        VideoEntity entity=get(videoId,profile.getLanguage());
        if(!entity.getProfileId().equals(profile.getId())){
            if(entity.getStatus().equals(AccessLevel.PRIVATE)){
                throw new AppMethodNotAllowedException(resourceBundleService.getMessage("method.not.allowed",profile.getLanguage()));
            }
        }
        increaseViewCount(videoId);
       return getFullInfo(videoId);
        // TODO: 8/20/2023  like dislike qo'shish
    }

    //  9. Get Video List Pagination (ADMIN)
    // TODO: 8/20/2023 playlist yozish
    public PageImpl<VideoDTO> getVideoList(Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"publishedDate"));
        Page<VideoShortInfoMapperI> pageObj=videoRepository.getVideoList(pageable);
       List<VideoDTO> dtoList=pageObj.getContent().stream().map(s->{
        VideoDTO dto = new VideoDTO();
        dto.setId(s.getVideoId());
        dto.setTitle(s.getVideoTitle());
        AttachDTO previewAttach= new AttachDTO();
        previewAttach.setId(s.getPreviewAttachId());
        previewAttach.setUrl(attachService.getUrl(s.getPreviewAttachId()));
        dto.setPreviewAttach(previewAttach);
        dto.setPublishedDate(s.getPublishedDate());
        ChannelDTO channelDTO= new ChannelDTO();
        channelDTO.setId(s.getChannelId());
        channelDTO.setName(s.getChannelName());
        channelDTO.setPhotoId(s.getChannelPhotoId());
        AttachDTO photo=new AttachDTO();
        photo.setId(s.getChannelPhotoId());
        photo.setUrl(attachService.getUrl(s.getChannelPhotoId()));
        channelDTO.setPhoto(photo);
        dto.setChannelDTO(channelDTO);
        dto.setViewCount(s.getViewCount());
        AttachDTO attach=new AttachDTO();
        attach.setDuration(s.getVideoDuration());
        dto.setAttach(attach);
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setId(s.getProfileId());
        profileDTO.setName(s.getProfileName());
        profileDTO.setSurname(s.getProfileSurname());
        dto.setProfile(profileDTO);
        //playlist
        return dto;
       }).toList();
       return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    public VideoEntity getByOwner(String videoId,Integer prtId, Language language){
        return videoRepository.findByIdAndProfileIdAndVisibleTrue(videoId,prtId)
                .orElseThrow(()->new ItemNotFoundException(resourceBundleService.getMessage("item.not.found",language)));
    }

    //10. Get Channel Video List Pagination (created_date desc)
    public PageImpl<VideoDTO> getByChannel(String channelId, Integer page, Integer size) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        List<AccessLevel>accessLevelList=new LinkedList<>();
        accessLevelList.add(AccessLevel.PUBLIC);
        if(profile!=null){
            ChannelEntity channel=channelService.get(channelId);
            if(channel.getProfileId().equals(profile.getId())){
              accessLevelList.add(AccessLevel.PRIVATE);

            }
        }
        Page<VideoPlaylistInfoMapperI> pageObj=videoRepository.getByChannelId(channelId,accessLevelList,pageable);

        List<VideoDTO> dtoList=pageObj.getContent().stream().map(s->{
            VideoDTO videoDTO= new VideoDTO();
            videoDTO.setId(s.getVideoId());
            videoDTO.setTitle(s.getVideoTitle());
            AttachDTO previewAttach= new AttachDTO();
            previewAttach.setId(s.getPreviewAttachId());
            previewAttach.setUrl(attachService.getUrl(s.getPreviewAttachId()));
            videoDTO.setPreviewAttach(previewAttach);
            videoDTO.setViewCount(s.getViewCount());
            videoDTO.setPublishedDate(s.getPublishedDate());
            AttachDTO attachDTO= new AttachDTO();
            attachDTO.setId(s.getAttachId());
            attachDTO.setUrl(attachService.getUrl(s.getAttachId()));
            attachDTO.setDuration(s.getDuration());
            videoDTO.setAttach(attachDTO);
            return videoDTO;
        }).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    public VideoEntity get(String videoId, Language language){
        return videoRepository.findByIdAndVisibleTrue(videoId)
                .orElseThrow(()->new ItemNotFoundException(resourceBundleService.getMessage("item.not.found",language)));
    }
    private VideoDTO getFullInfo(String videoId){
        VideoFullInfoMapperI m=videoRepository.getById(videoId);
        VideoDTO dto= new VideoDTO();
        dto.setId(m.getVideoId());
        dto.setTitle(m.getVideoTitle());
        dto.setDescription(m.getVideoDescription());
        AttachDTO previewAttachDto=new AttachDTO();
        previewAttachDto.setId(m.getPreviewAttachId());
        previewAttachDto.setUrl(attachService.getUrl(m.getPreviewAttachId()));
        dto.setPreviewAttach(previewAttachDto);
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(m.getAttachId());
        attachDTO.setUrl(attachService.getUrl(m.getAttachId()));
        attachDTO.setDuration(m.getVideoDuration());
        dto.setAttach(attachDTO);
        CategoryDTO categoryDTO= new CategoryDTO();
        categoryDTO.setId(m.getCategoryId());
        categoryDTO.setName(m.getCategoryName());
        dto.setCategory(categoryDTO);
        dto.setTagDTOList(videoTagService.getByVideoId(videoId));
        dto.setPublishedDate(m.getPublishedDate());
        ChannelDTO channelDTO= new ChannelDTO();
        channelDTO.setId(m.getAttachId());
        channelDTO.setName(m.getChannelName());
        AttachDTO channelPhoto= new AttachDTO();
        channelPhoto.setId(m.getChannelPhotoId());
        channelPhoto.setUrl(attachService.getUrl(m.getChannelPhotoId()));
        channelDTO.setPhoto(channelPhoto);
        dto.setChannelDTO(channelDTO);
        dto.setViewCount(m.getViewCount());
        dto.setSharedCount(m.getSharedCount());
        dto.setLikeCount(m.getLikeCount());
        dto.setDislikeCount(m.getDislikeCount());
        return dto;
    }

}
