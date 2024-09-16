package com.mamully.toyProject.users.command.services;

import com.mamully.toyProject.users.command.entities.User;
import com.mamully.toyProject.users.command.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository; // 데이터베이스와 연결하는 Repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보를 데이터베이스에서 가져오는 로직
        User user = usersRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getUserPw(),
                new ArrayList<>() // 권한 설정 안함
        );
    }
}
