package com.biou.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主启动类
 *
 * @author biou
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan({"com.biou.project.mapper", "com.biou.mapper"})
@ComponentScan({"com.biou.project", "com.biou"})
@EnableScheduling
@EnableAspectJAutoProxy
public class BiouProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiouProjectApplication.class, args);
    }
} 