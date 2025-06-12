package com.biou.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主启动类
 *
 * @author biou
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.biou.project.mapper")
public class BiouProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiouProjectApplication.class, args);
    }
} 