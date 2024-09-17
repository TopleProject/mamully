package com.mamully.toyProject.users.command.DTO;

import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
