package com.mamully.toyProject.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    private final SecretKey secretKey;

    public JwtUtil() {
        // HS512에 적합한 비밀 키 생성
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }


    public String generateToken(Long userNum, String userId, String userName) {

        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + EXPIRATION_TIME);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId) // 사용자 ID를 주제로 설정
                .claim("userNum", userNum) // 회원 고유번호 추가
                .claim("userName", userName) // 회원 이름 추가
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512);

        return builder.compact();
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

    public boolean validateToken(String token, String userId) {

        String extractedUsername = extractUserId(token);
        return (extractedUsername.equals(userId) && !isTokenExpired(token));
    }
}
