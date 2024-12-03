package com.melodicbridge.melodic_bridge.controller;

import com.melodicbridge.melodic_bridge.domain.User;
import com.melodicbridge.melodic_bridge.dto.LoginRequest;
import com.melodicbridge.melodic_bridge.dto.LoginResponse;
import com.melodicbridge.melodic_bridge.security.JwtTokenProvider;
import com.melodicbridge.melodic_bridge.service.UserService;
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
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. 사용자가 보낸 username과 password로 로그인
            String token = userService.login(loginRequest.username(), loginRequest.password());

            // 2. 로그인 성공 시 JWT 토큰 반환
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (RuntimeException e) {
            // 3. 로그인 실패 시 401 Unauthorized 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
