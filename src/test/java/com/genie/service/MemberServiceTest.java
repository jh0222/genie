package com.genie.service;

import com.genie.dto.MemberFormDto;
import com.genie.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setMemberId("genie");
        memberFormDto.setMemberPassword("12341234");
        memberFormDto.setMemberName("지니");
        memberFormDto.setMemberEmail("genie@gmail.com");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveUserTest() {
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getMemberId(), saveMember.getMemberId());
        assertEquals(member.getMemberPassword(), saveMember.getMemberPassword());
        assertEquals(member.getMemberName(), saveMember.getMemberName());
        assertEquals(member.getMemberEmail(), saveMember.getMemberEmail());
        assertEquals(member.getMemberRole(), saveMember.getMemberRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateUserTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.",e.getMessage());
    }

}