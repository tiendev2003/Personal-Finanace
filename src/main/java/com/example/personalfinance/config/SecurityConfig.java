package com.example.personalfinance.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import com.example.personalfinance.config.auth.JWTAuthEntryPoint;
import com.example.personalfinance.config.auth.JWTAuthFilter;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injecting necessary dependencies through constructor
    private final JWTAuthEntryPoint authEntryPoint;
    private final CustomUserDetailsService userDetailsService;
    private final LogoutHandler logoutHandler;
    private final JWTGenerator jwtGenerator;

    // Constructor for initializing dependencies
    public SecurityConfig(CustomUserDetailsService userDetailsService,
            JWTAuthEntryPoint authEntryPoint,
            LogoutHandler logoutHandler,
            JWTGenerator jwtGenerator) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.logoutHandler = logoutHandler;
        this.jwtGenerator = jwtGenerator;
    }

    // Configures HTTP security for the application
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(authEntryPoint).accessDeniedPage("/403"))
                .authorizeHttpRequests(Authorize -> Authorize
                        .requestMatchers("/**", "oauth2/**", "/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll())
                .csrf(c -> c.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                    corsConfiguration.setAllowedHeaders(
                            Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization"));

                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setMaxAge(3600L);
                    return corsConfiguration;
                }))
                // Set a custom entry point for unauthenticated requests
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint))

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                .build(); // Build the security configuration
    }

    // Exposes the AuthenticationManager bean for authentication purposes
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Defines the PasswordEncoder bean using BCrypt hashing for secure password
    // storage
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthFilter jwtAuthenticationFilter() {
        return new JWTAuthFilter(jwtGenerator, userDetailsService);
    }
}
