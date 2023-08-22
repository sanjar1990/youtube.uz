package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CategoryDTO;
import com.example.dto.TagDTO;
import com.example.service.CategoryService;
import com.example.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;
//   1. Create Tag


    @PostMapping("/public")
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagDTO dto){
        return ResponseEntity.ok(tagService.create(dto));
    }
    //      2. Update Tag (ADMIN)

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO>update(@PathVariable String id,
                                                @RequestParam("name") String name){
        return ResponseEntity.ok(tagService.update(name,id));
    }
    //      3. Delete Tag (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String id){
        return ResponseEntity.ok(tagService.delete(id));
    }
    //      4. Tag List
    @GetMapping("/public/getAll")
    public ResponseEntity<List<TagDTO>>getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }
}
