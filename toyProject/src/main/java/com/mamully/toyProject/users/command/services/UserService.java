package com.mamully.toyProject.users.command.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mamully.toyProject.config.security.JwtUtil;
import com.mamully.toyProject.users.command.DTO.JwtToken;
import com.mamully.toyProject.users.command.DTO.LoginDTO;
import com.mamully.toyProject.users.command.DTO.UserDTO;
import com.mamully.toyProject.users.command.entities.User;
import com.mamully.toyProject.users.command.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository; // JPA 리포지토리 주입
    private final PasswordEncoder passwordEncoder; // 비밀번호 해시를 위한 인코더
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void joinUser(UserDTO userDTO) {
        // 중복 사용자 확인
        checkDuplicate(userDTO);

        // 비밀번호 해시 처리
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())); // 비밀번호 해시화

        // userDTO -> user 변환
        User user = userDTO.toEntity();

        // 사용자 정보 저장
        usersRepository.save(user);
        System.out.println(user.getUserPw());
    }

    public JwtToken loginUser(LoginDTO loginDTO)  {
       Optional<User> loginUser = usersRepository.findByUserId(loginDTO.getId());
        // 아이디 틀린경우
       if(loginUser.isEmpty()) {
           return null;
       }
       User user = loginUser.get();
        // 비밀번호 틀린 경우
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getUserPw())) {
            return null;
        }
        return jwtUtil.generateToken(user.getUserNum(),user.getUserId(),user.getUserName());
    }

    private void checkDuplicate(UserDTO user) {
        if (usersRepository.existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다."); // 예외 처리
        }
        if (usersRepository.existsByUserPhone(user.getPhone())) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다."); // 예외 처리
        }
        if (usersRepository.existsByUserEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다."); // 예외 처리
        }
    }

    public User findUserByUserId(String userId) {
        return usersRepository.findByUserId(userId).orElse(null);
    }
}
