package com.example.spring_rest_exam.security;

import com.example.spring_rest_exam.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig  {

    private final JwtTokenFilter jwtTokenVerifier;

    public SecurityConfig(JwtTokenFilter jwtTokenVerifier) {
        this.jwtTokenVerifier = jwtTokenVerifier;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(
                        (auth) -> auth.antMatchers("/swagger",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/index.html").permitAll()
                                .anyRequest().permitAll()
                );
        http.addFilterBefore(jwtTokenVerifier, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
