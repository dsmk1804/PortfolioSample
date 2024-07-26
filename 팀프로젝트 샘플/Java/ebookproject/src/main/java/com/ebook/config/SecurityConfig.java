package com.ebook.config;

import com.ebook.handler.CustomOAuth2SuccessHandler;
import com.ebook.service.user.CustomOAuth2Service;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableAsync
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2Login(config -> {
            config.loginPage("/user/login")
                    .successHandler(new CustomOAuth2SuccessHandler())
                    .permitAll();
//                    .defaultSuccessUrl("/main");
        });


        http.formLogin(config -> {
            config.loginPage("/user/login").usernameParameter("id")
                    .defaultSuccessUrl("/main");
        });
        http.logout(conf -> {
            conf.logoutUrl("/user/logout")
                    .logoutSuccessUrl("/main")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
        });

        http.logout(config -> {
            config.logoutUrl("/user/logout")
                    .logoutSuccessUrl("/main")
                    .deleteCookies("JSESSIONID") // Cookie 제거
                    .invalidateHttpSession(true) // Session 초기화
                    .clearAuthentication(true)
                    .permitAll();
        });

        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/user/mypage/**").authenticated()
                    .anyRequest().permitAll(); // 모든 경로는 인증 없이 가능
        });

        return http.build();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public RestOperations restOperations(){
        return new RestTemplate();
    }






}
