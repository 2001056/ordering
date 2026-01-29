package com.example.ordering.member.dtos;

import com.example.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDetailDto {

    private Long id;
    private String email;
    private String name;
    private String role;

    public static MemberDetailDto fromEntity(Member member) {
        return MemberDetailDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().name())
                .build();
    }
}
