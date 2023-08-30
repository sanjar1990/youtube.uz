package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.ReportCreateDTO;
import com.example.dto.ReportDTO;
import com.example.service.ReportService;
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
@RequestMapping("/api/v1/report")
@Tag(name = "Comment", description = "Comment api list.")
public class ReportController {
    @Autowired
    private ReportService reportService;
    //1. Create report (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create report", description = "This api used for creating report ...")
    public ResponseEntity<ApiResponseDTO>create(@Valid @RequestBody ReportCreateDTO dto){
        return ResponseEntity.ok(reportService.create(dto));
    }
    //  2. ReportList Pagination ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    @Operation(summary = "get report pagination", description = "This api used for getting report pagination ...")
    public ResponseEntity<PageImpl<ReportDTO>>pagination(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                         @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(reportService.pagination(page-1,size));
    }
    //   3. Remove Report by id (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{reportId}")
    @Operation(summary = "delete report ", description = "This api used for deleting report ...")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String reportId){
        return ResponseEntity.ok(reportService.delete(reportId));
    }
    // 4. Report List By User id (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reportList/{userId}")
    @Operation(summary = "get report list", description = "This api used for getting report list by user id ...")
    public ResponseEntity<List<ReportDTO>>reportList(@PathVariable Integer userId){
        return ResponseEntity.ok(reportService.reportList(userId));
    }
}
