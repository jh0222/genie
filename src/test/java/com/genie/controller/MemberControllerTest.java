package com.genie.controller;

import com.genie.dto.MemberFormDto;
import com.genie.entity.Member;
import com.genie.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String memberId, String memberPassword) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setMemberId(memberId);
        memberFormDto.setMemberPassword(memberPassword);
        memberFormDto.setMemberName("지니");
        memberFormDto.setMemberEmail("genie@gmail.com");
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String memberId = "genie";
        String memberPassword = "12341234";
        this.createMember(memberId,memberPassword);
        mockMvc.perform(formLogin().userParameter("memberId")
                        .loginProcessingUrl("/members/login")
                        .user(memberId).password(memberPassword))
                        .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String memberId = "genie";
        String memberPassword = "12341234";
        this.createMember(memberId,memberPassword);
        mockMvc.perform(formLogin().userParameter("memberId")
                        .loginProcessingUrl("/members/login")
                        .user(memberId).password("12345"))
                        .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}