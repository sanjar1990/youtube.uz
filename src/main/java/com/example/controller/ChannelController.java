package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.ChannelDTO;
import com.example.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;
//     1. Create Channel (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<ApiResponseDTO>create(@Valid @RequestBody ChannelDTO dto){
        return ResponseEntity.ok(channelService.create(dto));
    }
//    2. Update Channel ( USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}")
    public ResponseEntity<ApiResponseDTO>update(@PathVariable String channelId,
                                                @Valid @RequestBody ChannelDTO dto){
        return ResponseEntity.ok(channelService.update(channelId,dto));
    }
//    3. Update Channel photo ( USER and OWNER)
@PreAuthorize("hasRole('USER')")
@PutMapping("/updatePhoto/{channelId}")
public ResponseEntity<ApiResponseDTO>updateChannelPhoto(@PathVariable String channelId,
                                            @RequestParam("photoId") String photoId){
    return ResponseEntity.ok(channelService.updatePhoto(channelId,photoId));
}
//    4. Update Channel banner ( USER and OWNER)
@PreAuthorize("hasRole('USER')")
@PutMapping("/updateBanner/{channelId}")
public ResponseEntity<ApiResponseDTO>updateBannerPhoto(@PathVariable String channelId,
                                                        @RequestParam("photoId") String photoId){
    return ResponseEntity.ok(channelService.updateBanner(channelId,photoId));
}
//    5. Channel Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ChannelDTO>>pagination(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(channelService.pagination(page-1,size));
    }
//    6. Get Channel By Id
    @GetMapping("/public/getById/{channelId}")
    public ResponseEntity<ChannelDTO>getById(@PathVariable String channelId){
        return ResponseEntity.ok(channelService.getById(channelId));
    }
//    7. Change Channel Status (ADMIN,USER and OWNER)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/updateStatus/{channelId}")
    public ResponseEntity<ApiResponseDTO>updateStatus(@PathVariable String channelId){
        return ResponseEntity.ok(channelService.updateStatus(channelId));
    }
//    8. User Channel List (murojat qilgan userni)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getAll")
    public ResponseEntity<List<ChannelDTO>>getAll(){
        return ResponseEntity.ok(channelService.getAll());
    }
}
