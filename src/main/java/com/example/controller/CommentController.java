package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.dto.CommentUpdateDTO;
import com.example.service.CommentService;
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
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment", description = "Comment api list.")
public class CommentController {
    @Autowired
    private CommentService commentService;

//     1. Crate Comment (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create comment", description = "This api used for creating comment ...")
    public ResponseEntity<CommentDTO>create(@Valid @RequestBody CommentCreateDTO dto){
        return ResponseEntity.ok(commentService.create(dto));
    }
//2. Update Comment (USER AND OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{commentId}")
    @Operation(summary = "update comment", description = "This api used for updating comment ...")
public ResponseEntity<CommentDTO>update(@PathVariable String commentId,
                                        @Valid @RequestBody CommentUpdateDTO dto){
    return ResponseEntity.ok(commentService.update(commentId,dto));
    }
    // 3. Delete Comment (USER AND OWNER, ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{commentId}")
    @Operation(summary = "delete comment", description = "This api used for deleting comment ...")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String commentId){
        return ResponseEntity.ok(commentService.delete(commentId));
    }
    //4. Comment List Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    @Operation(summary = "pagination comment", description = "This api used for pagination comment ...")
    public ResponseEntity<PageImpl<CommentDTO>>pagination(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(commentService.pagination(page-1,size));
    }
    //5. Comment List By profileId(ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationByProfileId/{prtId}")
    @Operation(summary = "paginationByProfileId comment", description = "This api used for paginationByProfileId comment ...")
    public ResponseEntity<PageImpl<CommentDTO>>paginationByProfileId(@PathVariable Integer prtId,
                                                                     @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                     @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(commentService.paginationByProfileId(prtId,page-1,size));
    }
    //6. Comment List By Profile (murojat qilgan odamning comment lari) (USER AND OWNER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/commentList")
    @Operation(summary = "commentList", description = "This api used for commentList by id ...")
    public ResponseEntity<List<CommentDTO>>commentList(){
        return ResponseEntity.ok(commentService.commentList());
    }
    //7. Comment List by videoId
    @GetMapping("/public/byVideoId/{videoId}")
    @Operation(summary = "commentList byVideoId", description = "This api used for commentList by VideoId  ...")
    public ResponseEntity<List<CommentDTO>>byVideoId(@PathVariable String  videoId){
        return ResponseEntity.ok(commentService.byVideoId(videoId));
    }
    // 8. Get Comment Replied Comment by comment Id (Commentga yozilgan commentlar)
    @GetMapping("/public/getReply/{commentId}")
    @Operation(summary = " getReply commentList", description = "This api used for getReply commentList by comment id  ...")
    public ResponseEntity<List<CommentDTO>>getReply(@PathVariable String commentId){
        return ResponseEntity.ok(commentService.getReply(commentId));
    }
}
