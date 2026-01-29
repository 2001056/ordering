package com.example.ordering.member.dtos;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginDto {

    private String email;
    private String password;

    private String accessToken;
}
