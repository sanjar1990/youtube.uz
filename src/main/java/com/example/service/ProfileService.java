package com.example.service;

import com.example.dto.*;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private AttachService attachService;
    @Value("${attach.default.photo}")
    private String defaultPhotoId;
    //1. Change password
    public ApiResponseDTO changePassword(AuthDTO authDTO) {
        Integer profileId= SpringSecurityUtil.getProfileId();
        Optional<ProfileEntity> optional=profileRepository.findById(profileId);
        if(optional.isEmpty()) {
        return new ApiResponseDTO(true,"profile not found");
        }
        ProfileEntity entity=optional.get();
            if(MD5Util.encode(entity.getPassword()).equals(MD5Util.encode(authDTO.getPassword()))){
                return new ApiResponseDTO(true, "password is same");
            }
            entity.setPassword(MD5Util.encode(authDTO.getPassword()));

        profileRepository.save(entity);
        return new ApiResponseDTO(false,"password changed");
    }

    public ApiResponseDTO updateEmail(String newEmail) {
        ProfileEntity entity=SpringSecurityUtil.getProfileEntity();
        if(entity.getEmail().equals(newEmail)){
            return new ApiResponseDTO(true,"email is same");
        }
        Optional<ProfileEntity>optional=profileRepository.findByEmailAndVisibleTrue(newEmail);
        if(optional.isPresent()) return new ApiResponseDTO(true,"email is already taken");

        entity.setEmail(newEmail);

        mailSenderService.sendUpdateEmailVerification(entity);
        return new ApiResponseDTO(false,"verification link is sent to your email");
    }

    public String updateEmailVerification(String jwt) {
        JwtDTO jwtDTO= JWTUtil.decode(jwt);
        Optional<ProfileEntity>optional=profileRepository.findById(jwtDTO.getId());
        if(optional.isEmpty()) throw new ItemNotFoundException("profile not found");
        ProfileEntity entity=optional.get();
        if(entity.getEmail().equals(jwtDTO.getEmail())){
            return "email already updated";
        }
        entity.setEmail(jwtDTO.getEmail());
        profileRepository.save(entity);
        return "email is changed successfully";
    }
        //3. Update Profile Detail(name,surname)
    public ApiResponseDTO updateDetails(String name, String surname) {
        ProfileEntity entity=SpringSecurityUtil.getProfileEntity();
        entity.setName(name);
        entity.setSurname(surname);
        profileRepository.save(entity);
        ProfileDTO dto=new ProfileDTO();
        dto.setName(name);
        dto.setSurname(surname);
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setAttachUrl(attachService.getUrl(entity.getAttachId()));
        return new ApiResponseDTO(false, dto);
    }
//4. Update Profile Attach (main_photo) (delete old attach)
    public ApiResponseDTO updatePhoto(String newPhotoId) {
        ProfileEntity entity=SpringSecurityUtil.getProfileEntity();
        String oldPhotoId=entity.getAttachId();
        if(oldPhotoId.equals(newPhotoId)){
            return new ApiResponseDTO(false,"photo already installed!");
        }
        attachService.get(newPhotoId);
        entity.setAttachId(newPhotoId);
        profileRepository.save(entity);
        if(!oldPhotoId.equals(defaultPhotoId)){
            attachService.delete(oldPhotoId);
        }
        ProfileDTO dto=new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setAttachUrl(attachService.getUrl(entity.getAttachId()));
        return new ApiResponseDTO(false,dto);
    }
    //5. Get Profile Detail (id,name,surname,email,main_photo((url)))
    public ApiResponseDTO getDetails() {
    ProfileEntity entity=SpringSecurityUtil.getProfileEntity();
    ProfileDTO dto=new ProfileDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setSurname(entity.getSurname());
    dto.setEmail(entity.getEmail());
    dto.setAttachUrl(attachService.getUrl(entity.getAttachId()));
    return new ApiResponseDTO(false,dto);
    }
//  6. Create Profile (ADMIN)
    public ApiResponseDTO create(ProfileDTO dto) {
        Optional<ProfileEntity>optional=profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if(optional.isPresent()) throw new AppBadRequestException("Email is used");
        if(dto.getRole()==null)throw new AppBadRequestException("role is required");
        ProfileEntity entity=new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setRole(dto.getRole());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        if(dto.getAttachId()==null || dto.getAttachId().isBlank()){
            entity.setAttachId("75b02981-915d-4446-994e-e3ecbc04298f");
        }else {
            entity.setAttachId(dto.getAttachId());
        }
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setPrtId(SpringSecurityUtil.getProfileId());
        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setAttachUrl(attachService.getUrl(dto.getAttachId()));
        return new ApiResponseDTO(false,dto);
    }
}
