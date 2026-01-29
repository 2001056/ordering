package com.example.ordering.member.controller;

import com.example.ordering.member.dtos.*;
import com.example.ordering.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/create")
    public ResponseEntity<Long> createMember(
            @RequestBody MemberCreateDto dto
    ) {
        Long memberId = memberService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberId);
    }
    @PostMapping("/dologin")
    public ResponseEntity<?> login(
            @RequestBody MemberLoginDto dto
    ) {
        String token = memberService.login(dto);
        return ResponseEntity.ok(token);
    }
    // 마이페이지 (내 정보 조회)
    @GetMapping("/myinfo")
    public ResponseEntity<MemberDetailDto> myInfo() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long memberId = Long.parseLong(authentication.getName());

        MemberDetailDto dto = memberService.getMemberDetail(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dto);
    }

    // 회원 목록 조회 (관리자)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public List<MemberListDto> getMemberList() {
        return memberService.getMemberList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/detail/{id}")
    public MemberDetailDto getMemberDetail(@PathVariable Long id) {
        return memberService.getMemberDetail(id);
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody MemberUpdatePasswordDto dto
    ) {
        memberService.changePassword(id, dto);
        return ResponseEntity.ok().build();
    }

}
