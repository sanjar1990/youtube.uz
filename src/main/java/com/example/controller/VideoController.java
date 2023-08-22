package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.VideoCreateDTO;
import com.example.dto.VideoDTO;
import com.example.dto.VideoUpdateDTO;
import com.example.enums.Language;
import com.example.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
@Tag(name = "Video", description = "Video api list.")

public class VideoController {
    @Autowired
    private VideoService videoService;
    // 1. Create Video (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create video", description = "This api used for creating video ...")
    public ResponseEntity<VideoDTO>create(@Valid @RequestBody VideoCreateDTO dto,
                                          @RequestParam("lang")Language language){
        return ResponseEntity.ok(videoService.create(dto,language));
    }
    // 2. Update Video Detail (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{videoId}")
    @Operation(summary = "update video", description = "This api used for updating video ...")
    public ResponseEntity<ApiResponseDTO> updateVideo(@PathVariable("videoId") String videoId,
                                      @RequestBody VideoUpdateDTO videoUpdateDTO,
                                      @RequestParam("lang") Language language){
        return ResponseEntity.ok(videoService.update(videoId,videoUpdateDTO,language));
    }

    //  3. Change Video Status (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateStatus/{videoId}")
    @Operation(summary = "update status video", description = "This api used for updating status video ...")
    public ResponseEntity<ApiResponseDTO> updateStatus(@PathVariable("videoId") String videoId,
                                                      @RequestParam("lang") Language language){
        return ResponseEntity.ok(videoService.updateStatus(videoId,language));
    }

    //4. Increase video_view Count by id

    //  5. Get Video Pagination by CategoryId
    @GetMapping("/public/paginationByCategory/{categoryId}")
    @Operation(summary = "Pagination by CategoryId", description = "This api used for Get Video Pagination by CategoryId ...")
    public ResponseEntity<PageImpl<VideoDTO>>paginationByCategory(@PathVariable Integer categoryId,
                                                                  @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                  @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(videoService.paginationByCategory(categoryId,page-1,size));
    }
    //6. Search video by Title
    @GetMapping("/public/getByTitle")
    @Operation(summary = "Search video by Title", description = "This api used for Search video by Title ...")
    public ResponseEntity<List<VideoDTO>>getByTitle(@RequestParam("title")String title){
        return ResponseEntity.ok(videoService.getByTitle(title));
    }
    // 7. Get video by tag_id with pagination
    @GetMapping("/public/getByTag/{tagId}")
    @Operation(summary = "Get video by tag_id with pagination", description = "This api used for Get video by tag_id with pagination")
    public ResponseEntity<PageImpl<VideoDTO>> getByTag(@PathVariable String tagId,
                                                       @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                       @RequestParam(value = "size",defaultValue = "10") Integer size ){
        return ResponseEntity.ok(videoService.getByTag(tagId,page-1,size));
    }
    // 8. Get Video By id (If Status PRIVATE allow only for OWNER or ADMIN)
    @GetMapping("/public/getById/{videoId}")
    @Operation(summary = " Get Video By id", description = "This api used for  Get Video By id")
    public ResponseEntity<VideoDTO>getById(@PathVariable String videoId,
                                           @RequestParam("lang") Language language){
        return ResponseEntity.ok(videoService.getById(videoId,language));
    }
    // 8. Get Video By id with registration (If Status PRIVATE allow only for OWNER or ADMIN)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getById/{videoId}")
    @Operation(summary = " Get Video By id owner", description = "This api used for  Get Video By id owner")
    public ResponseEntity<VideoDTO>getByIdOwner(@PathVariable String videoId){
        return ResponseEntity.ok(videoService.getByIdOwner(videoId));
    }
    //9. Get Video List Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getVideoList")
    @Operation(summary = " Get Video List Pagination (ADMIN)", description = "This api used for  Get Video List Pagination (ADMIN)")
    public ResponseEntity<PageImpl<VideoDTO>>getVideoList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(videoService.getVideoList(page-1,size));
    }
    //  10. Get Channel Video List Pagination (created_date desc)
    @GetMapping("/public/getByChannelId/{channelId}")
    @Operation(summary = " Get Video List Pagination (ADMIN)", description = "This api used for  Get Video List Pagination (ADMIN)")
    public ResponseEntity<PageImpl<VideoDTO>>getByChannelId(@PathVariable String channelId,
                                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(videoService.getByChannel(channelId,page-1,size));
    }
    //  10. Get Channel Video List Pagination owner(created_date desc)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getByChannelId/{channelId}")
    @Operation(summary = " Get Video List Pagination login (ADMIN)", description = "This api used for  Get Video List Pagination login (ADMIN)")
    public ResponseEntity<PageImpl<VideoDTO>>getByChannelIdLogin(@PathVariable String channelId,
                                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(videoService.getByChannel(channelId,page-1,size));
    }



}
