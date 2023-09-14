package com.practice.boardpratice.security;

import com.practice.boardpratice.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    CustomUserDetailsService customUserDetailsService;
    CustomOAuth2UserService customOAuth2UserService;

    public SpringSecurity(CustomUserDetailsService customUserDetailsService, CustomOAuth2UserService customOAuth2UserService) {
        this.customUserDetailsService = customUserDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers("/favicon.ico", "/resources/**");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                .formLogin(formLogin -> {
                    //권한이 필요한 요청은 해당 url로 리다이렉트
                    formLogin.loginPage("/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .failureUrl("/login")
                            .defaultSuccessUrl("/");
                })
                .logout(formLogout -> {
                    formLogout
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true);
                })

                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/", "/login", "/register", "/search").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login/google")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)))

                .build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
