package com.genie.service;

import com.genie.dto.BoardFormDto;
import com.genie.entity.Board;
import com.genie.entity.Member;
import com.genie.repository.BoardRepository;
import com.genie.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class BoardServiceTest {
    @Autowired
    BoardService boardService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    public Member saveMember() {
        Member member = new Member();
        member.setMemberId("genie");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("게시판 등록 테스트")
    public void saveBoardTest() {
        Member member = saveMember();

        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setBoardTitle("제목입니다.");
        boardFormDto.setBoardContent("내용입니다. 1234 abcdef");

        Long boardId = boardService.board(boardFormDto, member.getMemberId());

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(board.getBoardTitle(), boardFormDto.getBoardTitle());
        assertEquals(board.getBoardContent(), boardFormDto.getBoardContent());
    }

}