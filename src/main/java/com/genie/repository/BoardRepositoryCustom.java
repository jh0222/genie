package com.genie.repository;

import com.genie.dto.BoardSearchDto;
import com.genie.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<Board> getBoardPage_count(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<Board> getBoardPage_commentno(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<Board> getBoardPage_commentyes(BoardSearchDto boardSearchDto, Pageable pageable);
}
