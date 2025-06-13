package com.biou.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置类
 * 
 * @author Jax
 * @since 2025-06-14
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 密码编码器Bean
     * 使用BCrypt算法，安全强度为10
     *
     * @return PasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 