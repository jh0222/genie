package com.genie.repository;

import com.genie.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberIdOrMemberEmail(String memberId, String memberEmail);
    Member findByMemberId(String memberId);
}
