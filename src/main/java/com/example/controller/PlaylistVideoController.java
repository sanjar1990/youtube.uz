package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.PlaylistCreateDTO;
import com.example.dto.PlaylistVideoCreateUpdateDTO;
import com.example.mapper.PlaylistVideoInfoMapper;
import com.example.service.PlaylistVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlistVideo")
@Tag(name = "PlaylistVideo", description = "PlaylistVideo api list.")
public class PlaylistVideoController {
    @Autowired
    private PlaylistVideoService playlistVideoService;
//     1. Create (User and Owner)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create PlaylistVideo", description = "This api used for creating PlaylistVideo ...")
    public ResponseEntity<ApiResponseDTO>create(@Valid @RequestBody PlaylistVideoCreateUpdateDTO dto){
        return ResponseEntity.ok(playlistVideoService.create(dto));
    }
    //2. Update
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{pvId}")
    @Operation(summary = "update PlaylistVideo", description = "This api used for updating PlaylistVideo ...")
    public ResponseEntity<ApiResponseDTO>update(@PathVariable String pvId,
                                                @Valid @RequestBody PlaylistVideoCreateUpdateDTO dto){
        return ResponseEntity.ok(playlistVideoService.update(pvId,dto));
    }
//  3. Delete PlayListVideo
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("")
    @Operation(summary = "delete PlaylistVideo", description = "This api used for delete PlaylistVideo ...")
    public ResponseEntity<ApiResponseDTO>delete(@RequestParam("playlistId")String  playlistId,
                                                @RequestParam("videoId") String videoId){
        return ResponseEntity.ok(playlistVideoService.delete(playlistId,videoId));
    }
    //    4. Get Video list by playListId (video status published)
    @GetMapping("/public/getByPlaylistId/{plyId}")
    @Operation(summary = "get by ply id", description = "This api used for get by ply id ...")
    public ResponseEntity<List<PlaylistVideoInfoMapper>>getByPlaylistId(@PathVariable String plyId){
        return ResponseEntity.ok(playlistVideoService.getByPlaylist(plyId));
    }

}
