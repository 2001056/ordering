package com.example.ordering.common.configs;

import com.example.ordering.common.auth.JwtTokenFilter;
import com.example.ordering.common.auth.JwtTokenProvider;
import com.example.ordering.common.exception.JwtAuthenticationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationHandler jwtAuthenticationHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(c->c.configurationSource(corsConfigurationSource()))
//                csrf 공격에 대한 방어 비활성화
                .csrf(AbstractHttpConfigurer::disable)
//                http basic은 email/password를 인코딩하여 인증(전송)하는 간단한 인증방식. 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
//                세션로그인방식 비활성화
                .sessionManagement(a->a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                token을 검증하고 , authentication 객체 생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e->e.authenticationEntryPoint(jwtAuthenticationHandler))
//                지정한 특정 url을 제외한 모든 요청에 대해서 authenticated(인증처리)하겠다 라는 의미
                .authorizeHttpRequests(a->a.requestMatchers("/member/create","/member/dologin").permitAll().anyRequest().authenticated())
                .build();

    }
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
//        허용가능한 도메인 목록 설정
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://www.llall.com"));
//        모든 HTTP메서드(GET,POST,OPTIONS 등) 허용
        configuration.setAllowedMethods(Arrays.asList("*"));
//        모든 헤더(Authorization,content-type등) 허용
        configuration.setAllowedHeaders(Arrays.asList("*"));
//        자격증명 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        모든  url패턴에 대해 위 cors 정책을 적용
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}