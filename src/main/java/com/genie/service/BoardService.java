package com.genie.service;

import com.genie.dto.BoardFormDto;
import com.genie.dto.BoardSearchDto;
import com.genie.entity.Board;
import com.genie.entity.Member;
import com.genie.repository.BoardRepository;
import com.genie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Long board(BoardFormDto boardFormDto, String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        Board board = Board.createBoard(boardFormDto, member);
        boardRepository.save(board);
        return board.getId();
    }

    public Long updateBoard(BoardFormDto boardFormDto) throws Exception{
        //게시글 수정
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);

        return board.getId();
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage_count(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage_count(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage_commentno(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage_commentno(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage_commentyes(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage_commentyes(boardSearchDto, pageable);
    }

    public BoardFormDto getBoardDtl(Long boardId){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        board.setBoardCount(board.getBoardCount()+1);
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        return boardFormDto;
    }
}
