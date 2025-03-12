package com.melodicbridge.melodic_bridge.controller;

import com.melodicbridge.melodic_bridge.domain.User;
import com.melodicbridge.melodic_bridge.dto.LoginRequest;
import com.melodicbridge.melodic_bridge.dto.LoginResponse;
import com.melodicbridge.melodic_bridge.dto.UserDTO;
import com.melodicbridge.melodic_bridge.security.JwtTokenProvider;
import com.melodicbridge.melodic_bridge.service.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            //System.out.println("Generated token: " + token);
            // 2. 로그인 성공 시 JWT 토큰 반환
            System.out.println(token);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (RuntimeException e) {
            // 3. 로그인 실패 시 401 Unauthorized 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        System.out.println(token);
        try {
            System.out.println("Received token: " + token); // 1. 토큰 확인
            String username = jwtTokenProvider.getUsernameFromToken(token);
            System.out.println("Extracted username: " + username); // 2. 유저네임 확인

            Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty()) {
                System.out.println("User not found for username: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 오류 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
