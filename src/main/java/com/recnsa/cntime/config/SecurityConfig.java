package com.recnsa.cntime.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recnsa.cntime.security.filter.JwtValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private ObjectMapper objectMapper;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/","/error", "/swagger-ui/**", "/signIn", "/login/oauth2/**", "/**", "/websocket/**", "/ws/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .cors(withDefaults())
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/oauth2/authorization/google")
                )
                .addFilterAfter(new JwtValidationFilter(objectMapper), SecurityContextHolderFilter.class);

        return http.build();
    }
}
