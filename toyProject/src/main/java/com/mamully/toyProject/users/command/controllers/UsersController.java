package com.mamully.toyProject.users.command.controllers;

import com.mamully.toyProject.users.command.DTO.UserDTO;
import com.mamully.toyProject.users.command.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService; // 서비스 클래스 주입

    // 회원 가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {

        userService.joinUser(userDTO); // 사용자 저장

        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 완료");
    }
}
