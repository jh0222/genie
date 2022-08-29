package com.genie.controller;

import com.genie.dto.BoardFormDto;
import com.genie.dto.CommentFormDto;
import com.genie.entity.Board;
import com.genie.entity.Geniecomment;
import com.genie.repository.BoardRepository;
import com.genie.repository.CommentRepository;
import com.genie.service.BoardService;
import com.genie.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentCotroller {
    private final BoardService boardService;
    private final CommentService commentService;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final BoardRepository boardRepository;
    @PostMapping(value = "/admin/comment/new/{boardId}")
    public String boardForm_reply(@Valid CommentFormDto commentFormDto, BindingResult bindingResult,
                                  Model model, @PathVariable("boardId") Long boardId){
        if(bindingResult.hasErrors()){
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("board", boardFormDto);
            return "board/boardDtl";
        }

        Long commentId;
        try {
            commentId = commentService.comment(commentFormDto, boardId);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "board/boardDtl";
        }

        return "redirect:/board/{boardId}";
    }

    @PostMapping(value = "/admin/comment/delete/{commentId}")
    public @ResponseBody ResponseEntity commentDelete( @PathVariable("commentId") Long commentId, Model model, HttpServletResponse response){
        try {
            Optional<Geniecomment> geniecomment = commentRepository.findById(commentId);
            Long boardId = geniecomment.get().getBoard().getId();
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            board.setCommentStatus(0);
            commentRepository.deleteById(commentId);
        } catch (Exception e){
            model.addAttribute("errorMessage", "답글 삭제 중 에러가 발생하였습니다.");
        }
        return new ResponseEntity<Long>(commentId, HttpStatus.OK);
    }
}
