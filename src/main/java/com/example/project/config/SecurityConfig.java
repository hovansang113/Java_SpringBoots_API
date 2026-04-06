package com.example.project.config;

import com.example.project.security.JwtFilter;
import com.example.project.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // Spring Security 7: DaoAuthenticationProvider nhận UserDetailsService qua constructor
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/schools/**").hasRole("ADMIN")
                .requestMatchers("/api/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/parents").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/parents/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/parents/**").hasRole("ADMIN")
                .requestMatchers("/api/subjects/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/score-details").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.PUT, "/api/score-details/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.DELETE, "/api/score-details/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.POST, "/api/lesson-logs").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.PUT, "/api/lesson-logs/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.DELETE, "/api/lesson-logs/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.POST, "/api/academic-records").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.PUT, "/api/academic-records/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.PUT, "/api/report-cards/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.GET, "/api/report-cards/**").hasAnyRole("ADMIN", "TEACHER", "PARENT", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/score-details/**").hasAnyRole("ADMIN", "TEACHER", "PARENT", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/academic-records/**").hasAnyRole("ADMIN", "TEACHER", "PARENT", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/lesson-logs/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "TEACHER", "PARENT", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/parents/**").hasAnyRole("ADMIN", "TEACHER", "PARENT")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
