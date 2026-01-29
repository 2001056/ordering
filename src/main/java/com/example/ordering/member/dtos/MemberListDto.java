package com.example.ordering.member.dtos;

import com.example.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberListDto {

    private Long id;
    private String email;
    private String name;
    private String role;

    public static MemberListDto fromEntity(Member member) {
        return MemberListDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().name())
                .build();
    }
}
