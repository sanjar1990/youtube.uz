package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.ChannelDTO;
import com.example.entity.ChannelEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ChannelStatus;
import com.example.enums.ProfileRole;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.ChannelRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
//     1. Create Channel (USER)
     public ApiResponseDTO create(ChannelDTO dto) {
         Integer prtId=SpringSecurityUtil.getProfileId();
         ChannelEntity entity=new ChannelEntity();
         entity.setName(dto.getName());
         entity.setDescription(dto.getDescription());
         entity.setPhotoId(dto.getPhotoId());
         entity.setBannerId(dto.getBannerId());
         entity.setStatus(ChannelStatus.ACTIVE);
         entity.setProfileId(prtId);
         channelRepository.save(entity);
         dto.setId(entity.getId());
         dto.setCreatedDate(dto.getCreatedDate());
         dto.setProfileId(prtId);
         return new ApiResponseDTO(false,dto);
     }
    //    2. Update Channel ( USER and OWNER)
    public ApiResponseDTO update(String channelId,ChannelDTO dto) {
        ChannelEntity entity=get(channelId);
        Integer prtId=SpringSecurityUtil.getProfileId();
        checkOwner(entity.getProfileId(),prtId);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        channelRepository.save(entity);
        dto.setId(channelId);
        dto.setProfileId(prtId);
        return new ApiResponseDTO(false,dto);
    }
    //    3. Update Channel photo ( USER and OWNER)
    public ApiResponseDTO updatePhoto(String channelId, String photoId) {
        ChannelEntity entity=get(channelId);
        Integer prtId=SpringSecurityUtil.getProfileId();
        checkOwner(entity.getProfileId(),prtId);
       entity.setPhotoId(photoId);
       channelRepository.save(entity);
       return new ApiResponseDTO(false,"photo is updated");
    }
    //    4. Update Channel banner ( USER and OWNER)
    public ApiResponseDTO updateBanner(String channelId, String photoId) {
        ChannelEntity entity=get(channelId);
        Integer prtId=SpringSecurityUtil.getProfileId();
        checkOwner(entity.getProfileId(),prtId);
        entity.setBannerId(photoId);
        channelRepository.save(entity);
        return new ApiResponseDTO(false,"photo is updated");
    }

    //    5. Channel Pagination (ADMIN)
    public PageImpl<ChannelDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<ChannelEntity>pageObj=channelRepository.findAll(pageable);
        List<ChannelDTO>dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    //    6. Get Channel By Id
    public ChannelDTO getById(String channelId) {
         ChannelEntity entity=get(channelId);
         return toDto(entity);
    }
    //    7. Change Channel Status (ADMIN,USER and OWNER)
    public ApiResponseDTO updateStatus(String channelId) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        ChannelEntity channel=get(channelId);
        if(channel.getProfileId().equals(profile.getId())
                || profile.getRole().equals(ProfileRole.ROLE_ADMIN)){
            if(channel.getStatus().equals(ChannelStatus.ACTIVE)){
                channel.setStatus(ChannelStatus.BLOCK);
            }else {
                channel.setStatus(ChannelStatus.ACTIVE);
            }
            channelRepository.save(channel);
            return new ApiResponseDTO(false,toDto(channel));
        }
        return new ApiResponseDTO(true, "you don't have permission to update status");
    }
    //    8. User Channel List (murojat qilgan userni)
    public List<ChannelDTO> getAll() {
         Integer prtId=SpringSecurityUtil.getProfileId();
        List<ChannelEntity>entityList=channelRepository.findAllByProfileIdAndVisibleTrue(prtId);
        return entityList.stream().map(s->toDto(s)).toList();
    }

    private ChannelDTO toDto(ChannelEntity entity){
         ChannelDTO dto=new ChannelDTO();
         dto.setName(entity.getName());
         dto.setDescription(entity.getDescription());
         dto.setBannerId(entity.getBannerId());
         dto.setPhotoId(entity.getPhotoId());
         dto.setProfileId(entity.getProfileId());
         dto.setCreatedDate(entity.getCreatedDate());
         return dto;
    }

    private ChannelEntity get(String id){
         return channelRepository
                 .findByIdAndVisibleTrue(id).orElseThrow(()->new ItemNotFoundException("channel not found"));
    }
    //check channel owner
    private void checkOwner(Integer ownerId,Integer prtId){
        if(!ownerId.equals(prtId)){
           throw new AppBadRequestException("This channel not belongs to you!");
    }
    }





}
