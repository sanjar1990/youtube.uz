package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CommentLikeDTO;
import com.example.service.CommentLikeService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/commentLike")
@Tag(name = "Comment", description = "Comment api list.")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;
//1. Create Comment like
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like/{commentId}")
    @Operation(summary = "like comment", description = "This api used for creating like comment ...")
    public ResponseEntity<ApiResponseDTO>like(@PathVariable String commentId){
        return ResponseEntity.ok(commentLikeService.like(commentId));
    }
    //1. Create Comment dislike
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/dislike/{commentId}")
    @Operation(summary = "dislike comment", description = "This api used for creating dislike comment ...")
    public ResponseEntity<ApiResponseDTO>dislike(@PathVariable String commentId){
        return ResponseEntity.ok(commentLikeService.dislike(commentId));
    }
    //  3. User Liked Comment List (order by created_date desc) (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/ownLikeList")
    @Operation(summary = "Liked Comment List", description = "This api used for get Liked Comment List ...")
    public ResponseEntity<List<CommentLikeDTO>>getOwnLikeList(){
        Integer prtId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentLikeService.getLikeCommentList(prtId));
    }
    //4. Get User LikedComment List By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userLikeList/{userId}")
    @Operation(summary = "Liked Comment List by admin", description = "This api used for get Liked Comment List by admin...")
    public ResponseEntity<List<CommentLikeDTO>>userLikeList(@PathVariable Integer userId){
        return ResponseEntity.ok(commentLikeService.getLikeCommentList(userId));
    }
}
