package com.example.service;
import com.example.dto.*;
import com.example.entity.PlaylistEntity;
import com.example.entity.PlaylistVideoEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.VideoEntity;
import com.example.enums.ProfileRole;
import com.example.exception.AppBadRequestException;
import com.example.exception.AppMethodNotAllowedException;
import com.example.mapper.PlaylistShortInfoMapperI;
import com.example.mapper.PlaylistVideoInfoMapper;
import com.example.mapper.PlaylistVideoInfoMapperI;
import com.example.repository.PlaylistRepository;
import com.example.repository.PlaylistVideoRepository;
import com.example.repository.VideoRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistVideoService {
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private AttachService attachService;
//1. Create (User and Owner)
    public ApiResponseDTO create(PlaylistVideoCreateUpdateDTO dto) {
        ProfileEntity profile= SpringSecurityUtil.getProfileEntity();
        VideoEntity video=videoService.get(dto.getVideoId(), profile.getLanguage());
        if(!video.getProfileId().equals(profile.getId())){
            return new ApiResponseDTO(true,resourceBundleService.getMessage("video.not.belong",profile.getLanguage()));
        }
        PlaylistEntity playlist=playlistService.get(dto.getPlaylistId(),profile.getId(),profile.getLanguage());
        PlaylistVideoEntity entity= new PlaylistVideoEntity();
        entity.setPlaylistId(playlist.getId());
        entity.setVideoId(video.getId());
        Optional<PlaylistVideoEntity>optional=playlistVideoRepository
                .findByPlaylistIdAndOrderNumAndVisibleTrue(playlist.getId(), dto.getOrderNum());
        if(optional.isPresent()){
           throw new AppBadRequestException("order num is not available");
        }
        Boolean isExists=playlistVideoRepository.existsByPlaylistIdAndVideoIdAndVisibleTrue(playlist.getId(), video.getId());
        if(isExists) {
            return new ApiResponseDTO(true,resourceBundleService.getMessage("item.already.exists",profile.getLanguage()));
        }
        entity.setOrderNum(dto.getOrderNum());
        playlistVideoRepository.save(entity);
        PlaylistVideoDTO playlistVideoDTO= new PlaylistVideoDTO();
        playlistVideoDTO.setId(entity.getId());
        playlistVideoDTO.setPlaylistId(dto.getPlaylistId());
        playlistVideoDTO.setVideoId(dto.getVideoId());
        playlistVideoDTO.setOrderNum(dto.getOrderNum());
        playlistVideoDTO.setCreatedDate(entity.getCreatedDate());
    return new ApiResponseDTO(false,playlistVideoDTO);
    }

    public List<PlaylistShortInfoMapperI>getByPlaylistId(String plyId) {
        return playlistVideoRepository.getByPlaylistId(plyId);
    }

//2. Update
    public ApiResponseDTO update(String pvId, PlaylistVideoCreateUpdateDTO dto) {
        ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
        PlaylistVideoEntity entity=playlistVideoRepository.get(profile.getId(),pvId);
        VideoEntity video=videoService.get(dto.getVideoId(),profile.getLanguage());
        if(!video.getProfileId().equals(profile.getId())){
            return new ApiResponseDTO(true,resourceBundleService.getMessage("video.not.belong",profile.getLanguage()));
        }
        entity.setVideoId(dto.getVideoId());
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setOrderNum(dto.getOrderNum());
        playlistVideoRepository.save(entity);
        return new ApiResponseDTO(false,"updated");
    }
//  3. Delete PlayListVideo
    public ApiResponseDTO delete(String playlistId, String videoId) {
    ProfileEntity profile=SpringSecurityUtil.getProfileEntity();
    int n=playlistVideoRepository.deletePV(playlistId,videoId);
        assert profile != null;
        if(!profile.getRole().equals(ProfileRole.ROLE_ADMIN)){
        PlaylistEntity playlist=playlistService.get(playlistId, profile.getId(), profile.getLanguage());
        if(playlist==null) throw new AppMethodNotAllowedException(resourceBundleService
                .getMessage("video.not.belong",profile.getLanguage()));
    }
    return n>0?new ApiResponseDTO(false,"deleted"):new ApiResponseDTO(true,"not deleted");
    }
//4. Get Video list by playListId (video status published)
    public List<PlaylistVideoInfoMapper> getByPlaylist(String plyId) {
        List<PlaylistVideoInfoMapperI> mapper=playlistVideoRepository.getPlaylistInfo(plyId);
        return mapper.stream().map(s->{
            PlaylistVideoInfoMapper mapper1=new PlaylistVideoInfoMapper();
            mapper1.setPlaylistId(s.getPlaylistId());
            VideoDTO videoDTO= new VideoDTO();
            videoDTO.setId(s.getVideoId());
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(s.getPreviewAttachId());
            attachDTO.setUrl(attachService.getUrl(s.getPreviewAttachId()));
            videoDTO.setPreviewAttach(attachDTO);
            videoDTO.setTitle(s.getVideoTitle());
           AttachDTO attach=new AttachDTO();
           attach.setDuration(s.getDuration());
            videoDTO.setAttach(attach);
            mapper1.setVideo(videoDTO);
            ChannelDTO channelDTO= new ChannelDTO();
            channelDTO.setId(s.getChannelId());
            channelDTO.setName(s.getChannelName());
            mapper1.setChannel(channelDTO);
            mapper1.setCreatedDate(s.getCreatedDate());
            mapper1.setOrderNum(s.getOrderNum());
            return mapper1;
        }).toList();
    }
}
