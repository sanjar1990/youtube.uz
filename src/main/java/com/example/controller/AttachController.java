package com.example.controller;
import com.example.dto.ApiResponseDTO;
import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
@Tag(name = "Attach", description = "Attach api list.")
public class AttachController {
    @Autowired
    private AttachService attachService;
//    1. Create Attach (upload)
    @PostMapping("/public/upload")
    @Operation(summary = "upload photo", description = "This api used for uploading attaches ...")
    public ResponseEntity<AttachDTO>upload(@RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(attachService.upload(file));
    }
    //2. Get Attach By Id (Open)
    @GetMapping(value = "/public/{id}/img", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] openByIdImage(@PathVariable String id){
        return attachService.openById(id);
    }
    //3. Download Attach (Download)
    @GetMapping(value = "/public/{id}/general",produces = MediaType.ALL_VALUE)
    public byte[]openByIdGeneral(@PathVariable("id") String id){
        return attachService.loadByIdGeneral(id);
    }
    //3. Download Attach (Download)
    @GetMapping("/public/download/{id}")
    public ResponseEntity<Resource>download(@PathVariable String id){
    return attachService.download(id);
    }
    // 4. Attach pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<AttachDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(attachService.pagination(page-1,size));
    }
    // 5. Delete Attach (delete from db and system) (ADMIN)
   @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String id){
        return ResponseEntity.ok(attachService.delete(id));
    }
}
