package com.genie.entity;

import com.genie.dto.BoardFormDto;
import com.genie.dto.CommentFormDto;
import com.genie.dto.MemberFormDto;
import com.genie.repository.BoardRepository;
import com.genie.repository.CommentRepository;
import com.genie.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.yml")
class BoardTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    public void auditingTest(){
        Board newBoard = new Board();
        boardRepository.save(newBoard);

        em.flush();
        em.clear();

        Board board = boardRepository.findById(newBoard.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + board.getCreatedTime());
        System.out.println("update time : " + board.getModifiedTime());
        System.out.println("create member : " + board.getCreatedBy());
        System.out.println("modify member : " + board.getModifiedBy());
    }

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setMemberId("genie");
        memberFormDto.setMemberPassword("12341234");
        memberFormDto.setMemberName("지니");
        memberFormDto.setMemberEmail("genie@gmail.com");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    public Board createBoard(Member member){
        Board board = new Board();
        board.setMember(member);
        board.setBoardTitle("제목입니다.");
        board.setBoardContent("내용입니다.");
        board.setBoardCount(0);
        board.setCommentStatus(0);
        return board;
    }

    public Geniecomment createComment(Board board){
        Geniecomment geniecomment = new Geniecomment();
        geniecomment.setBoard(board);
        geniecomment.setCommentContent("답글입니다.");
        return geniecomment;
    }
    @Test
    @DisplayName("게시판 회원 엔티티 매핑 조회 테스트")
    public void findBoardAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Board board = new Board();
        board.setMember(member);
        boardRepository.save(board);

        em.flush();
        em.clear();

        Board savedBoard = boardRepository.findById(board.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedBoard.getMember().getId(), member.getId());
    }

    @Test
    @DisplayName("게시판 답글 엔티티 매핑 조회 테스트")
    public void findBoardAndCommentTest(){
        Member member = createMember();
        memberRepository.save(member);

        Board board = createBoard(member);
        boardRepository.save(board);

        Geniecomment geniecomment = createComment(board);
        commentRepository.save(geniecomment);

        em.flush();
        em.clear();

        assertEquals(geniecomment.getBoard().getId(), board.getId());
    }
}