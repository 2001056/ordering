package com.example.ordering.member.service;

import com.example.ordering.common.auth.JwtTokenProvider;
import com.example.ordering.common.exception.CustomException;
import com.example.ordering.common.exception.ErrorCode;
import com.example.ordering.member.dtos.*;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Member login(MemberLoginDto dto) {
        Optional<Member> optmember = memberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if (!optmember.isPresent()){
            check = false;
        }else {
            if (!passwordEncoder.matches(dto.getPassword(),optmember.get().getPassword())){
                check = false;
            }
        }

        if(!check){
            throw new IllegalArgumentException("email/pw is wrong");
        }
        return optmember.get();

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
    public MemberDetailDto myinfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Member> optMember = memberRepository.findByEmail(email);
        Member member = optMember.orElseThrow(() -> new EntityNotFoundException("entity is not found"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }
    // 회원 탈퇴 (Soft Delete)
    public void deleteMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

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
