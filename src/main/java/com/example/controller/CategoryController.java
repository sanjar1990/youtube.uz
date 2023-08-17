package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CategoryDTO;
import com.example.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
//      1. Create Category (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ApiResponseDTO>create(@Valid @RequestBody CategoryDTO dto){
       return ResponseEntity.ok(categoryService.create(dto));
   }
//    2. Update Category (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO>update(@PathVariable Integer id,
                                             @RequestParam("name") String name){
        return ResponseEntity.ok(categoryService.update(name,id));
    }
//    3. Delete Category (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.delete(id));
    }
//    4. Category List
    @GetMapping("/public/getAll")
    public ResponseEntity<List<CategoryDTO>>getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

}
