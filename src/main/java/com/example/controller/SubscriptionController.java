package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.SubCreateUpdateDTO;
import com.example.dto.SubscriptionDTO;
import com.example.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
@Tag(name = "Subscription", description = "Subscription api list.")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
//    1. Create User Subscription (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create sub", description = "This api used for creating sub ...")
    public ResponseEntity<SubscriptionDTO>create(@Valid @RequestBody SubCreateUpdateDTO dto){
        return ResponseEntity.ok(subscriptionService.create(dto));

    }
    // 2. Change Subscription Status (USER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/changeStatus")
    @Operation(summary = "change Status", description = "This api used for changing Status ...")
    public ResponseEntity<ApiResponseDTO>changeStatus(@RequestParam("channelId")String channelId){
        return ResponseEntity.ok(subscriptionService.changeStatus(channelId));
    }
//     3. Change Subscription Notification type (USER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/changeNotificationType")
    @Operation(summary = "changeNotificationType", description = "This api used for changing NotificationType ...")
    public ResponseEntity<ApiResponseDTO>changeNotificationType(@Valid @RequestBody SubCreateUpdateDTO dto){
    return ResponseEntity.ok(subscriptionService.changeNotificationType(dto));
    }

//  4. Get User Subscription List (only Active) (murojat qilgan user)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getSubList")
    @Operation(summary = "getSubList", description = "This api used for getting SubList ...")
    public ResponseEntity<List<SubscriptionDTO>>getSubList(){
        return ResponseEntity.ok(subscriptionService.getSubList());
    }
    //5. Get User Subscription List By UserId (only Active) (ADMIN)

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getUserSubList/{userId}")
    @Operation(summary = "getUserSubList", description = "This api used for getting User SubList ...")
    public ResponseEntity<List<SubscriptionDTO>>getUserSubList(@PathVariable Integer userId ){
        return ResponseEntity.ok(subscriptionService.getUserSubList(userId));
    }

}
