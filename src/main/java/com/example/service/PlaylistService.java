package com.example.service;

import com.example.dto.*;
import com.example.entity.ChannelEntity;
import com.example.entity.PlaylistEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.VideoEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.PlaylistInfoMapperI;
import com.example.mapper.PlaylistMapperI;
import com.example.mapper.PlaylistShortInfoMapperI;
import com.example.repository.PlaylistRepository;
import com.example.repository.PlaylistVideoRepository;
import com.example.util.SpringSecurityUtil;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private PlaylistVideoService playlistVideoService;
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;
// 1. Create Playlist (USER)
    public ApiResponseDTO create(PlaylistCreateDTO dto) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        Optional<PlaylistEntity>optional= playlistRepository
                .findByChannelIdAndOrderNumAndVisibleTrue(dto.getChannelId(), dto.getOrderNum());
    if(optional.isPresent()) {
        assert profile != null;
        return new ApiResponseDTO(true,resourceBundleService
                .getMessage("change.order.number",profile.getLanguage()));
    }
    PlaylistEntity entity=new PlaylistEntity();
    entity.setDescription(dto.getDescription());
    entity.setName(dto.getName());
    entity.setStatus(dto.getStatus());
    entity.setChannelId(dto.getChannelId());
    entity.setOrderNum(dto.getOrderNum());
    playlistRepository.save(entity);
    PlaylistDTO playlistDTO= new PlaylistDTO();
    playlistDTO.setName(dto.getName());
    playlistDTO.setDescription(dto.getDescription());
    playlistDTO.setStatus(dto.getStatus());
    playlistDTO.setChannelId(dto.getChannelId());
    playlistDTO.setOrderNum(dto.getOrderNum());
    playlistDTO.setId(entity.getId());
    return new ApiResponseDTO(false,playlistDTO);
    }
//      2. Update Playlist(USER and OWNER)
    public ApiResponseDTO update(String id, PlaylistUpdateDTO dto) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        PlaylistEntity entity=get(id,profile.getId(),profile.getLanguage());
       entity.setOrderNum(dto.getOrderNum());
       entity.setName(dto.getName());
       entity.setDescription(dto.getDescription());
      playlistRepository.save(entity);
      return new ApiResponseDTO(false,"playlist is updated");
    }
    // 3. Change Playlist Status (USER and OWNER)
    public ApiResponseDTO updateStatus(String id) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        assert profile != null;
        PlaylistEntity entity=get(id,profile.getId(),profile.getLanguage());
        if(entity.getStatus().equals(AccessLevel.PRIVATE)){
            entity.setStatus(AccessLevel.PUBLIC);
        }else {
            entity.setStatus(AccessLevel.PRIVATE);
        }
        playlistRepository.save(entity);
        return new ApiResponseDTO(false,"status is update");
    }
    //4. Delete Playlist (USER and OWNER, ADMIN)
    public ApiResponseDTO delete(String playlistId) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        int n;
        if(profile.getRole().equals(ProfileRole.ROLE_ADMIN)){
            n=playlistRepository.deletePlaylist(playlistId);
        }
        PlaylistEntity playlist=get(playlistId,profile.getId(),profile.getLanguage());
        n=playlistRepository.deletePlaylist(playlist.getId());
           return n>0?new ApiResponseDTO(false,"deleted"):new ApiResponseDTO(false,"not deleted");
    }

// 5. Playlist Pagination (ADMIN)
    public PageImpl<PlaylistDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<PlaylistInfoMapperI>pageObj=playlistRepository.pagination(pageable);
        List<PlaylistDTO> dtoList=pageObj.getContent().stream().map(this::getFullInfo).collect(Collectors.toList());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    //  6. Playlist List By UserId (order by order number desc) (ADMIN)
    public PageImpl<PlaylistDTO> paginationByUserId(Integer userId, Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"orderNum"));
        Page<PlaylistInfoMapperI>pageObj=playlistRepository.paginationByUser(pageable,userId);
        List<PlaylistDTO> dtoList=pageObj.getContent().stream().map(this::getFullInfo).collect(Collectors.toList());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    //         7. Get User Playlist (order by order number desc) (murojat qilgan user ni)

    public List<PlaylistDTO> getUserPlaylist() {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        List<PlaylistShortInfoMapperI> mapper=playlistRepository.getUserPlaylist(profile.getId());
        return getShortInfo(mapper);
    }
//8. Get Channel Play List By ChannelKey (order by order_num desc) (only Public)
    public List<PlaylistDTO> getByChannelId(String channelId) {
        List<PlaylistShortInfoMapperI>mapper=playlistRepository.getByChannel(channelId);
        return getShortInfo(mapper);
    }
//    9. Get Playlist by id
    public PlaylistMapper getById(String playlistId) {
        PlaylistMapperI mapper=playlistVideoRepository.getPlaylistById(playlistId);
        return new PlaylistMapper(mapper.getPlaylistId(), mapper.getPlaylistName(),
                mapper.getVideoCount(),mapper.getTotalViewCount(),mapper.getLastUpdateDate());
    }
    private List<PlaylistDTO>getShortInfo( List<PlaylistShortInfoMapperI> mapper){
       return mapper.stream().map(s->{
           PlaylistDTO dto= new PlaylistDTO();
           dto.setId(s.getPlaylistId());
           dto.setName(s.getPlaylistName());
           dto.setCreatedDate(s.getCreatedDate());
           ChannelDTO channelDTO= new ChannelDTO();
           channelDTO.setId(s.getChannelId());
           channelDTO.setName(s.getChannelName());
           dto.setChannel(channelDTO);
           List<PlaylistShortInfoMapperI>videoList=playlistVideoService.getByPlaylistId(s.getPlaylistId());
           List<VideoDTO>videoDTOList=videoList.stream().map(v->{
               VideoDTO videoDTO=new VideoDTO();
               videoDTO.setId(v.getVideoId());
               videoDTO.setTitle(v.getVideoName());
               AttachDTO attachDTO= new AttachDTO();
               attachDTO.setDuration(v.getDuration());
               videoDTO.setAttach(attachDTO);
               return videoDTO;
           }).toList();
           dto.setVideoDTOList(videoDTOList);
           dto.setVideoCount(videoList.size());
           return dto;
       }).toList();
    }
    private PlaylistDTO getFullInfo(PlaylistInfoMapperI m){
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(m.getPlaylistId());
        dto.setName(m.getPlaylistName());
        dto.setDescription(m.getDescription());
        dto.setStatus(m.getStatus());
        dto.setOrderNum(m.getOrderNum());
        ChannelDTO channelDTO= new ChannelDTO();
        channelDTO.setId(m.getChannelId());
        channelDTO.setName(m.getChannelName());
        AttachDTO attachDTO= new AttachDTO();
        attachDTO.setId(m.getChannelPhotoId());
        attachDTO.setUrl(attachService.getUrl(m.getChannelPhotoId()));
        channelDTO.setPhoto(attachDTO);
        dto.setChannel(channelDTO);
        ProfileDTO profileDTO= new ProfileDTO();
        profileDTO.setId(m.getProfileId());
        profileDTO.setName(m.getProfileName());
        profileDTO.setSurname(m.getSurname());
        AttachDTO profileAttach= new AttachDTO();
        profileAttach.setId(m.getProfilePhotoId());
        profileAttach.setUrl(attachService.getUrl(m.getProfilePhotoId()));
        profileDTO.setAttach(profileAttach);
        dto.setProfile(profileDTO);
        return dto;
    }
    //get
    public PlaylistEntity get(String plyId, Integer prtId, Language language){
        PlaylistEntity entity=playlistRepository.findByIdUser(prtId,plyId);
        if(entity==null){
            throw new ItemNotFoundException(resourceBundleService.getMessage("item.not.found",language));
        }
        return entity;
    }


}
