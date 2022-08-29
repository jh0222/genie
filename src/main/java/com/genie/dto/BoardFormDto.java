package com.genie.dto;

import com.genie.entity.Board;
import com.genie.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class BoardFormDto {
    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String boardTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다")
    private String boardContent;

    private Member member;

    private int boardCount;

    private int commentStatus;
    public LocalDateTime createdTime;
    private static ModelMapper modelMapper = new ModelMapper();

    public Board createBoard(){
        return modelMapper.map(this, Board.class);
    }

    public static BoardFormDto of(Board board) {
        return modelMapper.map(board, BoardFormDto.class);
    }
}
