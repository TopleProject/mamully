package com.mamully.toyProject.users.command.services;

import com.mamully.toyProject.users.command.DTO.UserDTO;
import com.mamully.toyProject.users.command.entities.User;
import com.mamully.toyProject.users.command.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository; // JPA 리포지토리 주입
    private final PasswordEncoder passwordEncoder; // 비밀번호 해시를 위한 인코더

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
    }

    private void checkDuplicate(UserDTO user) {
        if (usersRepository.existsByUserId(user.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다."); // 예외 처리
        }
        if (usersRepository.existsByUserPhone(user.getPhone())) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다."); // 예외 처리
        }
        if (usersRepository.existsByUserEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다."); // 예외 처리
        }
    }

    public User findUserByUsername(String username) {

        return usersRepository.findByUserId(username).orElse(null);
    }
}
