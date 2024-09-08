package com.mamully.toyProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/* spring security 사용 시 괄호 부분 삭제할 것!
*  지금 상태는 security를 사용하지 않아 잠시 설정해둔 상태임 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ToyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToyProjectApplication.class, args);
	}

}
