package com.example.service;

import com.example.dto.*;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private AttachService attachService;
    @Value("${attach.default.photo}")
    private String defaultPhotoId;


    public ApiResponseDTO registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional=profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if(optional.isPresent()){
            ProfileEntity entity=optional.get();
            if(!entity.getStatus().equals(ProfileStatus.REGISTRATION)){
                return new ApiResponseDTO(true,"email is already taken");
            }else {
                profileRepository.deleteById(entity.getId());
            }
        }
        ProfileEntity entity=new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setRole(ProfileRole.ROLE_USER);
        if(dto.getAttachId()!=null){
           entity.setAttachId(dto.getAttachId());
        }else {
            entity.setAttachId(defaultPhotoId);
        }
        profileRepository.save(entity);
        mailSenderService.sendEmailVerification(entity);

        return new ApiResponseDTO(false,"The verification link was send to your email");
    }

    public String emailVerification(String jwt) {
        JwtDTO jwtDTO= JWTUtil.decode(jwt);
        Optional<ProfileEntity> optional=profileRepository.findByEmail(jwtDTO.getEmail());
        if(optional.isEmpty()){
            throw new ItemNotFoundException("Profile not found!");
        }else {
            ProfileEntity entity=optional.get();
            if(!entity.getStatus().equals(ProfileStatus.REGISTRATION)){
                throw new AppBadRequestException("verification link is expired!");
            }
            entity.setStatus(ProfileStatus.ACTIVE);
            profileRepository.save(entity);
            return "you have been successfully verified!";
        }


    }
//2. Authorization
    public ApiResponseDTO login(AuthDTO authDTO) {
        Optional<ProfileEntity> optional=profileRepository
                .findByEmailAndPasswordAndVisibleTrue(authDTO.getEmail(), MD5Util.encode(authDTO.getPassword()));
        if(optional.isEmpty()) return new ApiResponseDTO(true, "profile not found");
        ProfileEntity entity=optional.get();
        if(!entity.getStatus().equals(ProfileStatus.ACTIVE)){
            return new ApiResponseDTO(true, "your status is not active");
        }
//        id,name,surname,email,main_photo (url)
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setId(entity.getId());
        profileDTO.setName(entity.getName());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setEmail(entity.getEmail());
        profileDTO.setAttachUrl(attachService.getUrl(entity.getAttachId()));
        profileDTO.setJwt(JWTUtil.encode(entity.getId(),entity.getEmail()));
        return new ApiResponseDTO(false,profileDTO);

    }
}
