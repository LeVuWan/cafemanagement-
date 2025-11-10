package com.windy.cafemanagement.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

/**
 * SecurityConfiguration
 *
 * Version 1.0
 *
 * Date: 11-10-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-10-2025 VuLQ Create
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

        /**
         * Password encoder bean
         * 
         * @return PasswordEncoder
         * 
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Security filter chain bean
         * 
         * @param http HttpSecurity
         * @return SecurityFilterChain
         * @throws Exception Exception
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                                .requestMatchers("/assets/**", "/css/**", "/js/**", "/img/**",
                                                                "/vendor/**", "/static/**", "/favicon.ico")
                                                .permitAll()
                                                .requestMatchers("/login**", "/error").permitAll()
                                                .requestMatchers("/admin/**")
                                                .hasAnyRole("EMPLOYY_SERVICE", "BARTENDER", "CASHER", "MANAGER")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/admin", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .rememberMe(r -> r
                                                .key("changeThisToAStrongKey")
                                                .tokenValiditySeconds(7 * 24 * 60 * 60));

                return http.build();
        }

        /**
         * Authentication manager bean
         * 
         * @param http               HttpSecurity
         * @param passwordEncoder    PasswordEncoder
         * @param userDetailsService UserDetailsService
         * @return AuthenticationManager
         * @throws Exception Exception
         */
        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                        UserDetailsService userDetailsService) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder);
                return authenticationManagerBuilder.build();
        }

}
