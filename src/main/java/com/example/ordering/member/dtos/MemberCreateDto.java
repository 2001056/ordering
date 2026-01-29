package com.example.ordering.member.dtos;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.domain.Role;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreateDto {

    private String email;
    private String password;
    private String name;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .role(Role.USER)
                .build();
    }
}