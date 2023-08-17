package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.dto.FilterEmailDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.repository.CustomEmailHistoryRepository;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailHistoryService {
@Autowired
    private EmailHistoryRepository emailHistoryRepository;
@Autowired
private CustomEmailHistoryRepository customEmailHistoryRepository;

    public void sendEmail(String text, String subject, ProfileEntity profile) {
        EmailHistoryEntity entity=new EmailHistoryEntity();
        entity.setMessage(text);
        entity.setToEmail(profile.getEmail());
        entity.setTitle(subject);
        emailHistoryRepository.save(entity);
    }
//    1. Get email pagination (ADMIN)
    public PageImpl<EmailHistoryDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<EmailHistoryEntity> pageObj=emailHistoryRepository.findAll(pageable);
        List<EmailHistoryDTO> dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    //2. Get Email pagination by email
    public PageImpl<EmailHistoryDTO> paginationByEmail(String email, Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<EmailHistoryEntity> pageObj=emailHistoryRepository.findByToEmailAndVisibleTrue(email,pageable);
        List<EmailHistoryDTO>dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    // 3. filter (email,created_date) + with pagination
    public PageImpl<EmailHistoryDTO> filterPagination(Integer page, Integer size, FilterEmailDTO filterEmailDTO) {
        FilterResultDTO<EmailHistoryEntity> filterResultDTO=
                customEmailHistoryRepository.filterPagination(page,size,filterEmailDTO);
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        List<EmailHistoryDTO> dtoList=filterResultDTO.getContent().stream().map(s->toDto(s)).toList();
        return new PageImpl<>(dtoList,pageable,filterResultDTO.getTotalElement());

    }

    private EmailHistoryDTO toDto(EmailHistoryEntity entity){
        EmailHistoryDTO dto=new EmailHistoryDTO();
        dto.setId(entity.getId());
        dto.setToEmail(entity.getToEmail());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


}
