package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.dto.FilterEmailDTO;
import com.example.service.EmailHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;
    //1. Get email pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<EmailHistoryDTO>>pagination(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(emailHistoryService.pagination(page-1,size));
    }
    // 2. Get Email pagination by email
    //    3. filter (email,created_date) + with pagination
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginationByEmail")
    public ResponseEntity<PageImpl<EmailHistoryDTO>>paginationByEmail(@RequestParam("email") String email,
                                                                      @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                                      @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(emailHistoryService.paginationByEmail(email,page-1,size));
    }
    // 3. filter (email,created_date) + with pagination
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filterPagination")
    public ResponseEntity<PageImpl<EmailHistoryDTO>>filterPagination(@RequestBody FilterEmailDTO filterEmailDTO,
                                                                     @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(emailHistoryService.filterPagination(page-1,size,filterEmailDTO));
    }

}
