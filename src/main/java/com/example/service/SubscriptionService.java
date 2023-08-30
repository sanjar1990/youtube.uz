package com.example.service;

import com.example.dto.*;
import com.example.entity.ProfileEntity;
import com.example.entity.SubscriptionEntity;
import com.example.enums.ActiveBlockStatus;
import com.example.enums.NotificationType;
import com.example.exception.ItemExistsException;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.SubInfoMapperI;
import com.example.repository.SubscriptionRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;
//    1. Create User Subscription (USER)
    public SubscriptionDTO create(SubCreateUpdateDTO dto) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        Optional<SubscriptionEntity>optional=subscriptionRepository
                .findByProfileIdAndChannelId(profile.getId(), dto.getChannelId());
        SubscriptionEntity entity;

        if(optional.isPresent()){
            entity = optional.get();
         entity.setVisible(true);


        }else {
            entity=new SubscriptionEntity();
            entity.setChannelId(dto.getChannelId());
            entity.setProfileId(profile.getId());
        }
        if (dto.getNotificationType() != null) {
            entity.setNotificationType(dto.getNotificationType());
        }
        subscriptionRepository.save(entity);
        return toDto(entity);
    }
    //2. Change Subscription Status (USER)
    public ApiResponseDTO changeStatus(String channelId) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        SubscriptionEntity entity=get(profile,channelId);
        ActiveBlockStatus status= entity.getStatus();
        if(status.equals(ActiveBlockStatus.ACTIVE)){
            entity.setStatus(ActiveBlockStatus.BLOCK);
        }else {
            entity.setStatus(ActiveBlockStatus.ACTIVE);
        }
        subscriptionRepository.save(entity);
        return new ApiResponseDTO(false,toDto(entity));
    }
    // 3. Change Subscription Notification type (USER)
    public ApiResponseDTO changeNotificationType(SubCreateUpdateDTO dto) {
        ProfileEntity profile = SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        SubscriptionEntity entity = get(profile, dto.getChannelId());
        entity.setNotificationType(dto.getNotificationType());
        if(dto.getNotificationType().equals(NotificationType.UNSUBSCRIBE)){
            entity.setUnsubscribeDate(LocalDateTime.now());
            entity.setVisible(false);
        }
        subscriptionRepository.save(entity);
        return new ApiResponseDTO(false,toDto(entity));
    }
    //4. Get User Subscription List (only Active) (murojat qilgan user)
    public List<SubscriptionDTO> getSubList() {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        List<SubInfoMapperI>mapper=subscriptionRepository.getSubList(profile.getId());
        return getList(mapper);
    }
    //5. Get User Subscription List By UserId (only Active) (ADMIN)
    public List<SubscriptionDTO> getUserSubList(Integer userId) {
        return getList(subscriptionRepository.getSubList(userId));
    }
    private SubscriptionDTO toDto(SubscriptionEntity entity){
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setChannelId(entity.getChannelId());
        dto.setStatus(entity.getStatus());
        dto.setProfileId(entity.getProfileId());
        dto.setNotificationType(entity.getNotificationType());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    private SubscriptionEntity get(ProfileEntity profile, String channelId){
        return subscriptionRepository
                .findByProfileIdAndChannelId(profile.getId(),
                        channelId).orElseThrow(()-> new ItemNotFoundException(resourceBundleService
                        .getMessage("item.not.found",profile.getLanguage())));
    }
    private List<SubscriptionDTO>getList(List<SubInfoMapperI> mapper){
        return mapper.stream().map(s->{
            SubscriptionDTO dto= new SubscriptionDTO();
            dto.setId(s.getSubId());
            ChannelDTO channelDTO= new ChannelDTO();
            channelDTO.setId(s.getChannelId());
            channelDTO.setName(s.getChannelName());
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(s.getChannelPhotoId());
            attachDTO.setUrl(attachService.getUrl(s.getChannelPhotoId()));
            channelDTO.setPhoto(attachDTO);
            dto.setChannel(channelDTO);
            dto.setNotificationType(s.getNotificationType());
            dto.setCreatedDate(s.getCreatedDate());
            return dto;
        }).toList();
    }

}
