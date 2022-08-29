package com.genie.controller;

import com.genie.dto.BoardFormDto;
import com.genie.dto.CommentFormDto;
import com.genie.entity.Board;
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
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    @Autowired
    private final BoardRepository boardRepository;

    @Autowired
    private final CommentRepository commentRepository;

    //유저가 새로운 게시판을 쓸 때
    @GetMapping(value = "/user/board/new")
    public String boardForm(Model model){
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "/board/boardForm";
    }

    @PostMapping(value = "/user/board/new")
    public String newBoard (@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model, Principal principal){
        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        String memberId = principal.getName();
        Long boardId;
        try {
            boardId = boardService.board(boardFormDto, memberId);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "board/boardForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/board/{boardId}")
    public String boardDtl(Model model, @PathVariable("boardId") Long boardId, Principal principal){
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
        //댓글이 있을 때
        if (boardFormDto.getCommentStatus() == 1 && principal != null) {
            CommentFormDto commentFormDto = commentService.getCommentDtl(boardId);

            if (principal.getName().equals("genie")) {
                model.addAttribute("loginIdCk_genie", 1);
            } else if (principal.getName().equals(boardFormDto.getMember().getMemberId())) {
                model.addAttribute("loginIdCk_member", 1);
            }
            model.addAttribute("commentFormDto", commentFormDto);
            model.addAttribute("board", boardFormDto);
        } else if (principal != null) {    // 댓글이 없을 때
            if (principal.getName().equals("genie")) {
                model.addAttribute("loginIdCk_genie", 1);
            } else if (principal.getName().equals(boardFormDto.getMember().getMemberId())) {
                model.addAttribute("loginIdCk_member", 1);
            }
            model.addAttribute("commentFormDto", new CommentFormDto());
            model.addAttribute("board", boardFormDto);
        } else {    //로그인하지 않았을 때
            model.addAttribute("commentFormDto", new CommentFormDto());
            model.addAttribute("board", boardFormDto);
        }
        return "board/boardDtl";
    }

    @GetMapping(value = "/user/board/update/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Long boardId, Model model){

        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "board/boardForm";
        }

        return "board/boardForm";
    }

    @PostMapping(value = "/user/board/update/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        try {
            boardService.updateBoard(boardFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "게시글 수정 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    @PostMapping(value = "/userandadmin/board/delete/{boardId}")
    public @ResponseBody ResponseEntity boardDelete(@PathVariable("boardId") Long boardId, Model model, HttpServletResponse response){
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);

            //답글 있을 떄
            if (board.getCommentStatus() == 1 ) {
                Long commentId = commentRepository.findByBoard(board).getId();
                commentRepository.deleteById(commentId);
            }

            boardRepository.deleteById(boardId);
        } catch (Exception e){
            model.addAttribute("errorMessage", "게시글 삭제 중 에러가 발생하였습니다.");
        }
        return new ResponseEntity<Long>(boardId, HttpStatus.OK);
    }

}
