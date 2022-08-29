package com.genie.repository;

import com.genie.constant.Role;
import com.genie.dto.BoardFormDto;
import com.genie.dto.MemberFormDto;
import com.genie.entity.Board;
import com.genie.entity.Member;
import com.genie.entity.QBoard;
import com.genie.entity.QGeniecomment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        Member member = new Member();
        member.setMemberId("genie");
        member.setMemberPassword("12341234");
        member.setMemberName("지니");
        member.setMemberEmail("genie@gmail.com");
        member.setMemberRole(Role.USER);
        member.setCreatedTime(LocalDateTime.now());
        member.setModifiedTime(LocalDateTime.now());
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    public void createBoardList(){
        Member member = createMember();
        for(int i=1;i<=10;i++){
            Board board = new Board();
            board.setMember(member);
            board.setBoardTitle("제목"+i);
            board.setBoardContent("내용"+i);
            board.setBoardCount(0);
            board.setCommentStatus(0);
            board.setCreatedTime(LocalDateTime.now());
            board.setModifiedTime(LocalDateTime.now());
            Board saveBoard = boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    public void queryDslTest(){
        this.createBoardList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBoard qBoard = QBoard.board;
        String searchQuery = "제목";
        JPAQuery<Board> query = queryFactory.selectFrom(qBoard)
                .where(QBoard.board.boardTitle.like("%" + searchQuery + "%"))
                .orderBy(QBoard.board.createdTime.desc());

        List<Board> boardList = query.fetch();

        for (Board board : boardList){
            System.out.println(board.toString());
        }
    }
}