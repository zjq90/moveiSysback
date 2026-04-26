package com.movie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 电影票务系统启动类
 * 基于Spring Boot 2.7.x重构
 * 
 * @author Movie Team
 */
@SpringBootApplication
@MapperScan("com.movie.mapper")
public class MovieApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieApplication.class, args);
        System.out.println("========================================");
        System.out.println("  电影票务系统后端启动成功!");
        System.out.println("  访问地址: http://localhost:8080/api");
        System.out.println("========================================");
    }
}
