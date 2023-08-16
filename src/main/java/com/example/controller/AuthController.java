package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    //1. Registration (with email verification)
    @PostMapping("/registration")
    public ResponseEntity<ApiResponseDTO>registration(@RequestBody ProfileDTO dto){
        return ResponseEntity.ok(authService.registration(dto));
    }
    // email verification
    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<String>emailVerification(@PathVariable String jwt){
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }
//    2. Authorization
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO>login(@Valid @RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(authService.login(authDTO));
    }
}
