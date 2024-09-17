package com.mamully.toyProject.users.command.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mamully.toyProject.config.security.JwtUtil;
import com.mamully.toyProject.users.command.DTO.JwtToken;
import com.mamully.toyProject.users.command.DTO.LoginDTO;
import com.mamully.toyProject.users.command.DTO.UserDTO;
import com.mamully.toyProject.users.command.entities.User;
import com.mamully.toyProject.users.command.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserLoginServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService; // 테스트할 서비스


    private LoginDTO loginDTO;
    private User user;
    @BeforeEach
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);

        // 로그인 DTO 설정
        loginDTO = new LoginDTO();
        loginDTO.setId("testUser");
        loginDTO.setPassword("password123");

        // 사용자 엔티티 설정
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode("password123");

        user = new User(1L,"testUser", encodePassword);


        when(passwordEncoder.matches("password123", user.getUserPw())).thenReturn(true);

        // 유저 리포지토리 모킹
        when(usersRepository.findByUserId("testUser")).thenReturn(Optional.of(user));

        // JWT 토큰 생성 모킹
        when(jwtUtil.generateToken(anyLong(), anyString(), anyString()))
                .thenReturn(new JwtToken("Bearer", "accessToken", "refreshToken"));


    }

    @DisplayName("로그인 성공 테스트")
    @Test
    public void testLoginUser_Success() {
        // 메서드 실행
        JwtToken token = userService.loginUser(loginDTO);

        // 검증
        assertNotNull(token); // JWT 토큰이 null이 아닌지 확인
        assertEquals("Bearer", token.getGrantType());
        assertEquals("accessToken", token.getAccessToken());
        assertEquals("refreshToken", token.getRefreshToken());
    }

    @DisplayName("로그인 실패 - 비밀번호 불일치 테스트")
    @Test
    public void testLoginUser_Fail_WrongPassword() throws JsonProcessingException {
        // 비밀번호 불일치 시나리오
        when(passwordEncoder.matches("wrongPassword", user.getUserPw())).thenReturn(false);
        loginDTO.setPassword("wrongPassword");

        // 메서드 실행
        JwtToken token = userService.loginUser(loginDTO);

        // 검증
        assertNull(token); // 비밀번호가 일치하지 않으므로 JWT 토큰이 null이어야 함
    }

    @DisplayName("로그인 실패 - 사용자 없음 테스트")
    @Test
    public void testLoginUser_Fail_UserNotFound() throws JsonProcessingException {
        when(usersRepository.findByUserId("wrongUser")).thenReturn(Optional.empty());

        loginDTO.setId("wrongUser");
        JwtToken token = userService.loginUser(loginDTO);

        // 검증
        assertNull(token); // 사용자가 존재하지 않으므로 JWT 토큰이 null이어야 함
    }
}
