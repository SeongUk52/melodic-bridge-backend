package com.melodicbridge.melodic_bridge.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.HstsConfig;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.XXssConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/h2-console/**").permitAll()  // H2 콘솔 경로 허용
                .anyRequest().permitAll()  // 모든 요청 허용
            )
            .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
            .headers(headers -> headers
                .httpStrictTransportSecurity(HstsConfig::disable)  // HSTS 비활성화 (필요시)
                .xssProtection(XXssConfig::disable) // XSS 보호 비활성화 (필요시)
                .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self' *"))  // iframe 허용 설정
            );

        return http.build(); // 필터 체인 반환
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000") // 클라이언트의 URL
                    .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                    .allowedHeaders("*");
        }
    }
}
