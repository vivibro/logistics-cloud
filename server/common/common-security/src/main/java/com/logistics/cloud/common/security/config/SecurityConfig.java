package com.logistics.cloud.common.security.config;

import com.logistics.cloud.common.security.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置
 * 1. 集成JWT校验
 * 2. 支持注解式权限控制（@PreAuthorize等）
 * 3. 关闭CSRF，允许跨域
 */
@Configuration
@EnableMethodSecurity // 启用方法级权限注解
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF
            .csrf().disable()
            // 关闭session，前后端分离用JWT
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 允许所有OPTIONS请求
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/error", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
            )
            // JWT认证过滤器（需后续实现）
            .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            // 异常处理（需后续实现）
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(new RestAccessDeniedHandler());
        return http.build();
    }

    // 如需自定义AuthenticationManager，可在此定义
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }
} 