package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.VideoLikeDTO;
import com.example.entity.ProfileEntity;
import com.example.service.VideoLikeService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videoLike")
@Tag(name = "videoLike", description = "videoLike api list.")
public class VideoLikeController {
    @Autowired
    private VideoLikeService videoLikeService;
//     1. Create Video like
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like/{videoId}")
    @Operation(summary = "create videoLike", description = "This api used for creating videoLike ...")
    public ResponseEntity<ApiResponseDTO>like(@PathVariable String videoId){
        return ResponseEntity.ok(videoLikeService.like(videoId));
    }
    //     1. Create Video dislike
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/dislike/{videoId}")
    @Operation(summary = "create video disLike", description = "This api used for creating videodisLike ...")
    public ResponseEntity<ApiResponseDTO>dislike(@PathVariable String videoId){
        return ResponseEntity.ok(videoLikeService.dislike(videoId));
    }
//     3. User Liked Video List (order by created_date desc) (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getOwnLikedVideo")
    @Operation(summary = "get Own LikedVideo", description = "This api used for getting Own Liked Video...")
    public ResponseEntity<List<VideoLikeDTO>>getOwnLikedVideo(){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(videoLikeService.getLikedVideo(prtId));
    }
    //4. Get User LikedVideo List By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getLikedVideo/{prtId}")
    @Operation(summary = "get LikedVideo", description = "This api used for getting Liked Video...")
    public ResponseEntity<List<VideoLikeDTO>>getLikedVideo(@PathVariable Integer prtId){
        return ResponseEntity.ok(videoLikeService.getLikedVideo(prtId));
    }

}
