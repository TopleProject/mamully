package com.mamully.toyProject.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("../")  // 현재 디렉토리에서 .env 파일 로드
                .load();
        System.setProperty("MYSQL_ROOT_PASSWORD", dotenv.get("MYSQL_ROOT_PASSWORD"));
        System.setProperty("MYSQL_DATABASE", dotenv.get("MYSQL_DATABASE"));
        System.setProperty("MYSQL_USER", dotenv.get("MYSQL_USER"));
        System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
    }
}
