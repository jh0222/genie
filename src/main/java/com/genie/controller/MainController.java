package com.genie.controller;

import com.genie.constant.Role;
import com.genie.dto.BoardSearchDto;
import com.genie.entity.Board;
import com.genie.entity.Member;
import com.genie.repository.MemberRepository;
import com.genie.service.BoardService;
import com.genie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @GetMapping(value = {"/", "/{page}"})
    public String main(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,   10);
        Page<Board> boards = boardService.getBoardPage(boardSearchDto, pageable);

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        //genie 계정 시작과 동시에 넣기, 한번만 실행
        if (memberRepository.findByMemberId("genie") == null) {
            try {
                Member member = Member.createAdmin(passwordEncoder);
                memberService.saveMember(member);
            } catch (IllegalStateException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "main";
            }
        }

        return "main";
    }

    @GetMapping(value = {"/order/count","/order/count/{page}"})
    public String orderCount(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boards = boardService.getBoardPage_count(boardSearchDto, pageable);

        model.addAttribute("order", 1);
        model.addAttribute("order_order", "order/count/");
        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    @GetMapping(value = {"/order/commentno","/order/commentno/{page}"})
    public String orderCommentno(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boards = boardService.getBoardPage_commentno(boardSearchDto, pageable);

        model.addAttribute("order", 2);
        model.addAttribute("order_order", "order/commentno/");
        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    @GetMapping(value = {"/order/commentyes","/order/commentyes/{page}"})
    public String orderCommentyes(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boards = boardService.getBoardPage_commentyes(boardSearchDto, pageable);

        model.addAttribute("order", 3);
        model.addAttribute("order_order", "order/commentyes/");
        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

}
