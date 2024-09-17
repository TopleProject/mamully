package com.mamully.toyProject.config.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mamully.toyProject.users.command.DTO.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtUtil {

    // 엑세스 & 리프레시 토큰 유효기간 설정
    private final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    private final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60*24*7; // 1주일

    private final SecretKey secretKey;

    public JwtUtil() {
        // HS512에 적합한 비밀 키 생성
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }


    public JwtToken generateToken(Long userNum, String userId, String userName) {

            System.out.println("jwtutill 진입완료");
            long now = System.currentTimeMillis();
            Date accessTokenExpiryDate = new Date(now + ACCESS_EXPIRATION_TIME);
            Date refreshTokenExpiryDate = new Date(now+REFRESH_EXPIRATION_TIME);

            String accessToken = Jwts.builder() // access 토큰
                    .setSubject(userId) // 사용자 ID를 주제로 설정
                    .claim("userNum", userNum) // 회원 고유번호 추가
                    .claim("userName", userName)
                    .setIssuedAt(new Date(now))
                    .setExpiration(accessTokenExpiryDate)
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();
            System.out.println(accessToken);
            String refreshToken = Jwts.builder() //refresh 토큰
                    .setExpiration(refreshTokenExpiryDate)
                    .signWith(secretKey,SignatureAlgorithm.HS512)
                    .compact();
            System.out.println(refreshToken);
            System.out.println("값 반환 직전");

            return JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }





    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // 비밀 키 설정
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserNum(String token) {

        return (Long) extractAllClaims(token).get("userNum");
    }

    public String extractUserName(String token) {

        return (String) extractAllClaims(token).get("userName");
    }
    public String extractUserId(String token) {

        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {

        return extractAllClaims(token).getExpiration().before(new Date());
    }

    //토큰 검증
    public boolean validateToken(String token, String userId) {
        String extractedUsername = extractUserId(token);
        return (extractedUsername.equals(userId) && !isTokenExpired(token));
    }


}
