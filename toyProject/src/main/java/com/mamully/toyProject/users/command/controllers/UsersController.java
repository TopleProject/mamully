package com.mamully.toyProject.users.command.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mamully.toyProject.config.security.JwtUtil;
import com.mamully.toyProject.users.command.DTO.JwtToken;
import com.mamully.toyProject.users.command.DTO.LoginDTO;
import com.mamully.toyProject.users.command.DTO.UserDTO;
import com.mamully.toyProject.users.command.entities.User;
import com.mamully.toyProject.users.command.repositories.UsersRepository;
import com.mamully.toyProject.users.command.services.UserService;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService; // 서비스 클래스 주입
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    // 회원 가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        // 유효성 검사 오류가 있는 경우
        if (bindingResult.hasErrors()) {
            // 모든 필드 오류의 메시지를 리스트로 반환
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }

        try{
            userService.joinUser(userDTO); // 사용자 저장
            return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 완료");
        } catch (IllegalArgumentException e){
            // 아이디, 이메일 등 중복일 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }


    }


    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginDTO loginDTO)  {
        JwtToken token = userService.loginUser(loginDTO);

        // 아이디 또는 비밀번호 오류
        if(token==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.findUserByUserId(loginDTO.getId());
        Map<String, String> loginResponse = new HashMap<>();

        // 엑세스 토큰 및 이름, 아이디 반환
        loginResponse.put("accessToken", token.getAccessToken());
        loginResponse.put("id", loginDTO.getId());
        loginResponse.put("name", user.getUserName());

        return ResponseEntity.ok(loginResponse); // 사용자 번호 및 JWT 토큰 반환.
    }


}
