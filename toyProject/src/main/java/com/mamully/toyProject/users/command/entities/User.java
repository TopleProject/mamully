package com.mamully.toyProject.users.command.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users") // 데이터베이스 테이블 이름
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNum;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userPhone;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    private String userStatus; // "가입" 또는 "탈퇴"

    @Column(nullable = false)
    private LocalDateTime userSignupDate; // 가입 날짜

    public User(String userName, String userPhone, String userEmail, String userId, String userPw, String userStatus, LocalDateTime userSignupDate) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPw = userPw;
        this.userStatus = userStatus;
        this.userSignupDate = userSignupDate;
    }

    public User(Long userNum, String userId, String userPw) {
        this.userNum = userNum;
        this.userId = userId;
        this.userPw = userPw;
    }
}

