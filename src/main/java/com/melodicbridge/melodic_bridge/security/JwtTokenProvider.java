package com.melodicbridge.melodic_bridge.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secretKey; // JWT 서명용 키 (환경 변수나 안전한 저장소에 저장 권장)
    private final long validityInMilliseconds = 3600000; // 1시간 유효 기간

    // 토큰 생성 메서드
    public String generateToken(String username) {
        Dotenv dotenv = Dotenv.configure().load();
        this.secretKey = dotenv.get("JWT_SECRET_KEY");
        Claims claims = Jwts.claims().setSubject(username); // 토큰에 사용자 정보 저장
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 서명 알고리즘과 키 설정
                .compact();
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}