package com.example.ordering.member.service;

import com.example.ordering.common.auth.JwtTokenProvider;
import com.example.ordering.member.dtos.*;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입
    public Long create(MemberCreateDto dto) {

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일");
        }

        String encodedPassword =
                passwordEncoder.encode(dto.getPassword());

        Member member = dto.toEntity(encodedPassword);

        memberRepository.save(member);

        return member.getId();
    }

    public String login(MemberLoginDto dto) {

        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호 틀림");
        }

        return jwtTokenProvider.createToken(
                member.getId(),
                member.getRole().name()
        );
    }

    // 회원 상세 조회 (마이페이지)
    @Transactional(readOnly = true)
    public List<MemberListDto> getMemberList() {
        return memberRepository.findAll()
                .stream()
                .map(MemberListDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberDetailDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 없음"));

        return MemberDetailDto.fromEntity(member);
    }

    // 회원 탈퇴 (Soft Delete)
    public void deleteMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.deleteMember();
    }
    public void changePassword(Long memberId, MemberUpdatePasswordDto dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 없음"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호 불일치");
        }

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        member.changePassword(encodedNewPassword);
    }
}
