package library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 關閉 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**", "/api/borrowing/**").permitAll()
                        .anyRequest().permitAll() // 所有路徑都不需要驗證
                );

        return http.build();
    }
}
