package com.genie.service;

import com.genie.dto.CommentFormDto;
import com.genie.dto.MemberFormDto;
import com.genie.entity.Board;
import com.genie.entity.Geniecomment;
import com.genie.entity.Member;
import com.genie.repository.BoardRepository;
import com.genie.repository.CommentRepository;
import com.genie.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class CommentServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

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

    public Board createBoard(Member member){
        Board board = new Board();
        board.setMember(member);
        board.setBoardTitle("제목입니다.");
        board.setBoardContent("내용입니다.");
        board.setBoardCount(0);
        board.setCommentStatus(0);
        return board;
    }

    @Test
    @DisplayName("답글 등록 테스트")
    public void saveCommentTest(){
        Member member = createMember();
        memberRepository.save(member);

        Board board = createBoard(member);
        boardRepository.save(board);

        CommentFormDto commentFormDto = new CommentFormDto();
        commentFormDto.setCommentContent("답글입니다.");

        Long commentId = commentService.comment(commentFormDto, board.getId());
        Geniecomment geniecomment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(geniecomment.getCommentContent(), commentFormDto.getCommentContent());
    }


}