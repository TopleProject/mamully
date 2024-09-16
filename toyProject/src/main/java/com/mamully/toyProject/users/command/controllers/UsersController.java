package com.mamully.toyProject.users.command.controllers;

import com.mamully.toyProject.config.security.JwtUtil;
import com.mamully.toyProject.users.command.DTO.LoginDTO;
import com.mamully.toyProject.users.command.DTO.UserDTO;
import com.mamully.toyProject.users.command.entities.User;
import com.mamully.toyProject.users.command.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService; // 서비스 클래스 주입
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final View error;

    // 회원 가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {

        userService.joinUser(userDTO); // 사용자 저장

        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 완료");
    }


    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            User user = userService.findUserByUsername(loginDTO.getUsername());

            final String jwt = jwtUtil.generateToken(user.getUserNum(), user.getUserId(), user.getUserName());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", jwt);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials"));
        }
    }

}
