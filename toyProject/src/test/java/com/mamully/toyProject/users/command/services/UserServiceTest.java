package com.mamully.toyProject.users.command.services;

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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository; // JPA 리포지토리 주입

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // 비밀번호 해시를 위한 인코더

    @InjectMocks
    private UserService userService; // 테스트할 서비스

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setName("테스트");
        userDTO.setUsername("testUser");
        userDTO.setPassword("password123");
        userDTO.setPhone("010-1234-5678");
        userDTO.setEmail("test@example.com");
    }

    @DisplayName("회원 저장 성공 테스트")
    @Test
    public void testJoinUser_Success() {
        // Mocking
        when(usersRepository.existsByUserId(userDTO.getUsername())).thenReturn(false);
        when(usersRepository.existsByUserPhone(userDTO.getPhone())).thenReturn(false);
        when(usersRepository.existsByUserEmail(userDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");

        // 메서드 실행
        userService.joinUser(userDTO);

        // 검증
        verify(usersRepository, times(1)).save(any(User.class)); // 사용자 정보 저장 호출 검증
        verify(passwordEncoder, times(1)).encode("password123"); // 비밀번호 해시화 호출 검증
    }

    @DisplayName("회원 아이디 중복 테스트")
    @Test
    public void testJoinUser_DuplicateUsername() {
        // Mocking
        when(usersRepository.existsByUserId(userDTO.getUsername())).thenReturn(true);

        // 예외 발생 검증
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.joinUser(userDTO);
        });

        // 예외 메시지 검증
        assertEquals("이미 사용 중인 아이디입니다.", thrown.getMessage());
    }

    @DisplayName("회원 핸드폰 중복 테스트")
    @Test
    public void testJoinUser_DuplicatePhone() {
        // Mocking
        when(usersRepository.existsByUserId(userDTO.getUsername())).thenReturn(false);
        when(usersRepository.existsByUserPhone(userDTO.getPhone())).thenReturn(true);

        // 예외 발생 검증
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.joinUser(userDTO);
        });

        // 예외 메시지 검증
        assertEquals("이미 사용 중인 전화번호입니다.", thrown.getMessage());
    }

    @DisplayName("회원 이메일 중복 테스트")
    @Test
    public void testJoinUser_DuplicateEmail() {
        // Mocking
        when(usersRepository.existsByUserId(userDTO.getUsername())).thenReturn(false);
        when(usersRepository.existsByUserPhone(userDTO.getPhone())).thenReturn(false);
        when(usersRepository.existsByUserEmail(userDTO.getEmail())).thenReturn(true);

        // 예외 발생 검증
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.joinUser(userDTO);
        });

        // 예외 메시지 검증
        assertEquals("이미 사용 중인 이메일입니다.", thrown.getMessage());
    }
}