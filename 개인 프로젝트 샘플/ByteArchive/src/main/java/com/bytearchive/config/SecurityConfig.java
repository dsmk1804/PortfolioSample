package com.bytearchive.config;

import com.bytearchive.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(config -> {
            config.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
        });

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/user/signup", "/user/auth", "/user/login", "/user/check", "/static/**").permitAll();
            request.anyRequest().authenticated();
        });

        http.formLogin(config -> {
            config.loginPage("/user/login")
                    .loginProcessingUrl("/user/login")
                    .usernameParameter("id")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureHandler((request, response, exception) -> {
                        response.sendRedirect("/login?error");
                    })
                    .permitAll();
        });

        http.logout(config -> {
            config.logoutUrl("/user/logout")
                    .logoutSuccessUrl("/user/login")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll();
        });

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // 테스트용
    }

    // 🔥 삭제됨
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     return customUserDetailsService;
    // }

}
