package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.TagDTO;
import com.example.dto.VideoTagDTO;
import com.example.enums.Language;
import com.example.mapper.VideoTagMapper;
import com.example.service.VideoTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videoTag")
@Tag(name = "VideoTag", description = "Video tag api list.")
public class VideoTagController {
    @Autowired
    private VideoTagService videoTagService;
    //  2. Delete tag from vide (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    @Operation(summary = "delete video_tag", description = "This api used for deleting video_tag ...")
    public ResponseEntity<ApiResponseDTO>delete(@Valid @RequestBody VideoTagDTO videoTagDTO,
                                                @RequestParam("lang")Language language){
    return ResponseEntity.ok(videoTagService.delete(videoTagDTO,language));
    }
//     3. Get video Tag List by videoId
    @GetMapping("/public/{videoId}")
    @Operation(summary = "delete video_tag", description = "This api used for deleting video_tag ...")
    public ResponseEntity<List<VideoTagMapper>>getTagList(@PathVariable String videoId){
        return ResponseEntity.ok(videoTagService.getAllList(videoId));
    }

}
