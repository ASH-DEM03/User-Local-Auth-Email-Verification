package com.Ash.user_local_auth_email_verification.controller;

import com.Ash.user_local_auth_email_verification.dto.UserDTO;
import com.Ash.user_local_auth_email_verification.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegistrationController {

    @Autowired
    private userService userService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {

       userService.register(userDTO);
       return ResponseEntity.ok("user created successfully");



    }

}
