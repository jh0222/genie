package com.genie.service;

import com.genie.entity.Member;
import com.genie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateUser(member);
//        passwordCheck(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateUser(Member member) {
        Member findUser = memberRepository.findByMemberIdOrMemberEmail(member.getMemberId(),member.getMemberEmail());
        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

//    private void passwordCheck(Member member){
//        if(member.getMemberPassword() != member.getMemberPasswordCheck()){
//            throw new IllegalStateException("비밀번호가 다릅니다.");
//        }
//    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId);

        if(member == null){
            throw new UsernameNotFoundException(memberId);
        }

        return User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPassword())
                .roles(member.getMemberRole().toString())
                .build();
    }

}
