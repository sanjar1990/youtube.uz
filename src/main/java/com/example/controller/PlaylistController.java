package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.PlaylistCreateDTO;
import com.example.dto.PlaylistDTO;
import com.example.dto.PlaylistUpdateDTO;
import com.example.service.PlaylistMapper;
import com.example.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlist")
@Tag(name = "Playlist", description = "Playlist api list.")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
// 1. Create Playlist (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create Playlist", description = "This api used for creating Playlist ...")
    public ResponseEntity<ApiResponseDTO>create(@Valid @RequestBody PlaylistCreateDTO dto){
        return ResponseEntity.ok(playlistService.create(dto));
    }
    //  2. Update Playlist(USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update Playlist", description = "This api used for Updating Playlist ...")
    public ResponseEntity<ApiResponseDTO>update(@PathVariable String id,
                                                @Valid @RequestBody PlaylistUpdateDTO dto){
        return ResponseEntity.ok(playlistService.update(id,dto));
    }

    // 3. Change Playlist Status (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "Change Playlist Status", description = "This api used for Updating Playlist Status ...")
    public ResponseEntity<ApiResponseDTO>updateStatus(@PathVariable String id){
        return ResponseEntity.ok(playlistService.updateStatus(id));
    }
    //4. Delete Playlist (USER and OWNER, ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{playlistId}")
    @Operation(summary = "Delete Playlist", description = "This api used for Delete Playlist ...")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String  playlistId){
        return ResponseEntity.ok(playlistService.delete(playlistId));
    }
    //    5. Playlist Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    @Operation(summary = "Playlist Pagination", description = "This api used for 5. Playlist Pagination (ADMIN) ...")
    public ResponseEntity<PageImpl<PlaylistDTO>>pagination(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                           @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(playlistService.pagination(page-1,size));
    }
    //  6. Playlist List By UserId (order by order number desc) (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationByUserId/{userId}")
    @Operation(summary = "Playlist List By UserId", description = "This api used for Playlist List By UserId ...")
    public ResponseEntity<PageImpl<PlaylistDTO>>paginationByUserId(@PathVariable Integer userId,
                                                                   @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                   @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(playlistService.paginationByUserId(userId,page-1,size));
    }
    //   7. Get User Playlist (order by order number desc) (murojat qilgan user ni)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserPlaylist")
    @Operation(summary = "Get User Playlist ", description = "This api used for Playlist List Get User Playlist ...")
    public ResponseEntity<List<PlaylistDTO>>getUserPlaylist(){
        return ResponseEntity.ok(playlistService.getUserPlaylist());
    }
//   8. Get Channel Play List By ChannelKey (order by order_num desc) (only Public)
    @GetMapping("/public/getByChannel/{channelId}")
    @Operation(summary = "Get Playlist by channel ", description = "This api used for  Get Playlist by channel ...")
    public ResponseEntity<List<PlaylistDTO>>getByChannel(@PathVariable String channelId){
        return ResponseEntity.ok(playlistService.getByChannelId(channelId));
    }
    // 9. Get Playlist by id
    @GetMapping("/public/getById/{playlistId}")
    @Operation(summary = "Get Playlist by id ", description = "This api used for  Get Playlist by id ...")
    public ResponseEntity<PlaylistMapper>getById(@PathVariable String playlistId){
        return ResponseEntity.ok(playlistService.getById(playlistId));
    }



}
