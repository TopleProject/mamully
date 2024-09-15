package com.mamully.toyProject.users.command.DTO;

import com.mamully.toyProject.users.command.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

//    @NotBlank(message = "이름은 필수입니다.")
    private String name; // 이름

//    @NotBlank(message = "이메일은 필수입니다.")
//    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email; // 이메일

//    @NotBlank(message = "핸드폰 번호는 필수입니다.")
//    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "핸드폰 번호는 010-0000-0000 형식이어야 합니다.")
    private String phone; // 핸드폰 번호

//    @NotBlank(message = "아이디는 필수입니다.")
//    @Size(min = 4, max = 20, message = "아이디는 4~20자리여야 합니다.")
    private String username; // 아이디

//    @NotBlank(message = "비밀번호는 필수입니다.")
//    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이어야 합니다.")
    private String password; // 비밀번호

//    @NotBlank(message = "인증 코드는 필수입니다.")
//    private String verificationCode; // 인증 코드

    // DTO를 Users 엔티티로 변환하는 메서드
    public User toEntity() {
        return new User(
                this.name,
                this.phone,
                this.email,
                this.username,
                this.password, // 비밀번호는 서비스에서 해시 처리해야 함
                "가입", // 기본 상태 설정
                LocalDateTime.now() // 가입 날짜 설정
        );
    }
}
