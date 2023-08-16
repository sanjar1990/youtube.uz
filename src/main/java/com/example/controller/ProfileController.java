package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    //1. Change password
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/changePassword")
    public ResponseEntity<ApiResponseDTO>changePassword(@Valid @RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(profileService.changePassword(authDTO));
    }
    //2. Update Email (with email verification)
  @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateEmail")
    public ResponseEntity<ApiResponseDTO>updateEmail(@RequestParam("email")String newEmail){
        return ResponseEntity.ok(profileService.updateEmail(newEmail));
  }
  //verification update email
    @GetMapping("/public/verification/updateEmail/{jwt}")
    public ResponseEntity<String>updateEmailVerification(@PathVariable String jwt){
        return ResponseEntity.ok(profileService.updateEmailVerification(jwt));
    }
    //3. Update Profile Detail(name,surname)
    @PutMapping("/updateDetails")
    public ResponseEntity<ApiResponseDTO>updateDetails(@RequestParam("name") String name,
                                                   @RequestParam("surname") String surname){
        return ResponseEntity.ok(profileService.updateDetails(name,surname));
    }
    //4. Update Profile Attach (main_photo) (delete old attach)
    @PutMapping("/updatePhoto")
    public ResponseEntity<ApiResponseDTO>updatePhoto(@RequestParam("photoId")String photoId){
        return ResponseEntity.ok(profileService.updatePhoto(photoId));
    }
    //5. Get Profile Detail (id,name,surname,email,main_photo((url)))
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO>getDetail(){
        return ResponseEntity.ok(profileService.getDetails());
    }
    //  6. Create Profile (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createProfile")
    public ResponseEntity<ApiResponseDTO>create(@RequestBody ProfileDTO profileDTO){
        return ResponseEntity.ok(profileService.create(profileDTO));
    }

}
