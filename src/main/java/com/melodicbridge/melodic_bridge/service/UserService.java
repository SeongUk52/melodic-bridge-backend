package com.melodicbridge.melodic_bridge.service;

import com.melodicbridge.melodic_bridge.domain.User;
import com.melodicbridge.melodic_bridge.repository.UserRepository;
import com.melodicbridge.melodic_bridge.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            //System.out.println("Found user: " + user.getUsername());

            if (!passwordEncoder.matches(password, user.getPassword())) {
                //System.out.println("Password does not match");
                throw new BadCredentialsException("Invalid password");
            }
            //System.out.println("Password matches");

            String token = jwtTokenProvider.generateToken(username);
            //System.out.println("Generated token in login: " + token);
            return token;
        } catch (Exception e) {
            //System.out.println("Error in login: " + e.getMessage());
            throw e;
        }
    }
}
