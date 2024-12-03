package com.melodicbridge.melodic_bridge.service;

import com.melodicbridge.melodic_bridge.domain.User;
import com.melodicbridge.melodic_bridge.repository.UserRepository;
import com.melodicbridge.melodic_bridge.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void registerUser(String username, String password, String email, String nickname) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setEmail(email);
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new IllegalArgumentException("Password does not match");
        }
        return jwtTokenProvider.generateToken(username);
    }
}
