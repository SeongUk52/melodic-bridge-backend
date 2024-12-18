package com.melodicbridge.melodic_bridge.controller;

import com.melodicbridge.melodic_bridge.domain.User;
import com.melodicbridge.melodic_bridge.dto.LoginRequest;
import com.melodicbridge.melodic_bridge.dto.LoginResponse;
import com.melodicbridge.melodic_bridge.security.JwtTokenProvider;
import com.melodicbridge.melodic_bridge.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getNickname());
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    try {
        // 1. 사용자가 보낸 username과 password로 로그인
        String token = userService.login(loginRequest.username(), loginRequest.password());

        // 2. HttpOnly 쿠키 생성 및 설정
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true); // HttpOnly 설정
        jwtCookie.setSecure(true);  // HTTPS에서만 동작 (개발 환경에서는 false로 설정 가능)
        jwtCookie.setPath("/");    // 애플리케이션 전체에 쿠키 적용
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효

        // 3. 응답에 쿠키 추가
        response.addCookie(jwtCookie);

        // 4. 응답 본문으로 메시지 반환 (선택 사항)
        return ResponseEntity.ok("로그인 성공");
    } catch (RuntimeException e) {
        // 5. 로그인 실패 시 401 Unauthorized 반환
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
}
