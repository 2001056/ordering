package com.example.ordering.member.dtos;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdatePasswordDto {

    private String currentPassword;
    private String newPassword;
}
