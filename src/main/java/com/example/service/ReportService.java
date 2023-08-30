package com.example.service;

import com.example.dto.*;
import com.example.entity.ReportEntity;
import com.example.enums.ReportType;
import com.example.mapper.ReportInfoMapperI;
import com.example.repository.ReportRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;
//1. Create report (USER)
    public ApiResponseDTO create(ReportCreateDTO dto) {
        Integer prtId= SpringSecurityUtil.getProfileId();
        ReportEntity entity=new ReportEntity();
        entity.setProfileId(prtId);
        entity.setContent(dto.getContent());
        if(dto.getChannelId()!=null){
            entity.setChannelId(dto.getChannelId());
            entity.setType(ReportType.CHANNEL);
        }
        if(dto.getVideoId()!=null){
            entity.setVideoId(dto.getVideoId());
            entity.setType(ReportType.VIDEO);
        }
        reportRepository.save(entity);
        return new ApiResponseDTO(false,"reported");
    }
//  2. ReportList Pagination ADMIN
    public PageImpl<ReportDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<ReportInfoMapperI> pageObj=reportRepository.pagination(pageable);
        List<ReportDTO> dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }

//   3. Remove Report by id (ADMIN)
    public ApiResponseDTO delete(String reportId) {
        int n=reportRepository.deleteReport(reportId);
        return n>0?new ApiResponseDTO(false,"deleted"):new ApiResponseDTO(true,"not deleted");
    }
// 4. Report List By User id (ADMIN)
    public List<ReportDTO> reportList(Integer userId) {
    return reportRepository.reportList(userId).stream()
            .map(this::toDto).collect(Collectors.toList());
    }
    private ReportDTO toDto(ReportInfoMapperI s){
        ReportDTO reportDTO= new ReportDTO();
        reportDTO.setId(s.getId());
        ProfileDTO profileDTO= new ProfileDTO();
        profileDTO.setId(s.getProfileId());
        profileDTO.setName(s.getProfileName());
        profileDTO.setSurname(s.getProfileSurname());
        AttachDTO attachDTO= new AttachDTO();
        attachDTO.setId(s.getProfilePhotoId());
        attachDTO.setUrl(attachService.getUrl(s.getProfilePhotoId()));
        profileDTO.setAttach(attachDTO);
        reportDTO.setProfile(profileDTO);
        reportDTO.setContent(s.getContent());
        reportDTO.setChannelId(s.getChannelId());
        reportDTO.setVideoId(s.getVideoId());
        reportDTO.setType(s.getType());
        reportDTO.setCreatedDate(s.getCreatedDate());
        return reportDTO;
    }
}
