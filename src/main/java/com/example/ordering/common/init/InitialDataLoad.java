package com.example.ordering.common.init;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.ordering.member.domain.Role;


@Component
@Transactional
public class InitialDataLoad implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Autowired
    public InitialDataLoad(PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByEmail("admin@naver.com").isPresent()){
            return;
        }
                memberRepository.save(Member.builder()
                .name("admin").role(Role.ADMIN)
                .email("admin@naver.com")
                .password(passwordEncoder.encode("123456789"))
                .build());
    }
}
