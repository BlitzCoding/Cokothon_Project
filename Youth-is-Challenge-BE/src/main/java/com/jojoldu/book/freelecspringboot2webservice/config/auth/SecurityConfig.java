package com.jojoldu.book.freelecspringboot2webservice.config.auth;

import com.jojoldu.book.freelecspringboot2webservice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //스프링 시큐리티 설정들 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/", "/css/**", "/scripts/**","/plugin/**", "/fonts/", "/loginPage", "/loginPage/").permitAll()
                .and()
                    .logout()
                .and()
                //[OAuth2 로그인 기능] 설정 진입점
                    .oauth2Login()
                    .userInfoEndpoint() //-> 여러 설정 중 로그인 성공 이후 사용자 정보 가져올 때 설정
                    .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속조치 진행할 UserService 인터페이스 구현체 등록
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
