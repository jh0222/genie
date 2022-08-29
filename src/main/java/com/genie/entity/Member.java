package com.genie.entity;

import com.genie.constant.Role;
import com.genie.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    @Id
    @Column(name="member_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    private String memberPassword;


    private String memberEmail;

    private String memberName;

    @Enumerated(EnumType.STRING)
    private Role memberRole;

    public static Member createAdmin(PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setMemberId("genie");
        String password = passwordEncoder.encode("12341234");
        member.setMemberPassword(password);
        member.setMemberName("genie");
        member.setMemberEmail("sjhyun@gsitm.com");
        member.setMemberRole(Role.ADMIN);
        return member;
    }
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setMemberId(memberFormDto.getMemberId());
        String password = passwordEncoder.encode(memberFormDto.getMemberPassword());
        member.setMemberPassword(password);
        member.setMemberName(memberFormDto.getMemberName());
        member.setMemberEmail(memberFormDto.getMemberEmail());
        member.setMemberRole(Role.USER);
        return member;
    }


}
