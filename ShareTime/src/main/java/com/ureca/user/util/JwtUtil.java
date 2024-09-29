package com.ureca.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ureca.user.dto.User;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}") // application.properties에서 비밀 키를 로드
    private String secretKey; 

    private Key SECRET_KEY;

    @PostConstruct
    private void initializeSecretKey() {
        try {
            if (secretKey != null && !secretKey.isEmpty()) {
                // Base64로 디코딩하여 키 초기화
                SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
                System.out.println("SECRET_KEY successfully initialized");
            } else {
                throw new IllegalArgumentException("비밀 키가 설정되지 않았습니다. 환경 변수를 확인하세요.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("비밀 키를 디코딩할 수 없습니다. 올바른 Base64 형식인지 확인하세요.");
            throw e;
        }
    }

    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1시간 (기존 30분에서 늘림)
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24시간

    // 액세스 토큰 생성
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getUser_id() != null ? user.getUser_id() : 0);
        return createToken(claims, user.getEmail(), ACCESS_TOKEN_VALIDITY);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getUser_id() != null ? user.getUser_id() : 0);
        return createToken(claims, user.getEmail(), REFRESH_TOKEN_VALIDITY);
    }

    // 토큰 생성 메소드
    private String createToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // 동일한 SECRET_KEY 사용
                .compact();
    }

    // 토큰에서 클레임 추출
    public Claims extractAllClaims(String token) {
        try {
            // SECRET_KEY로 서명 검증
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            System.out.println("Extracted claims from token: " + claims);
            return claims;
        } catch (Exception e) {
            System.out.println("Failed to extract claims: " + e.getMessage());
            throw e;
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // 토큰에서 사용자 ID 추출
    public Integer extractUserId(String token) {
        return (Integer) extractAllClaims(token).get("user_id");
    }
}