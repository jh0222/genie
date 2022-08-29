package com.genie.service;

import com.genie.dto.BoardFormDto;
import com.genie.dto.CommentFormDto;
import com.genie.entity.Board;
import com.genie.entity.Geniecomment;
import com.genie.repository.BoardRepository;
import com.genie.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long comment(CommentFormDto commentFormDto, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        Geniecomment comment = Geniecomment.createComment(commentFormDto, board);
        commentRepository.save(comment);
        board.setCommentStatus(1);
        return comment.getId();
    }

    public CommentFormDto getCommentDtl(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        Geniecomment geniecomment = commentRepository.findByBoard(board);
        CommentFormDto commentFormDto = CommentFormDto.of(geniecomment);
        return commentFormDto;
    }
}
