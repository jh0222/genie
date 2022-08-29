package com.genie.dto;

import com.genie.entity.Board;
import com.genie.entity.Geniecomment;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class CommentFormDto {
    private Long id;

    @NotBlank(message = "답글은 필수 입력 값입니다.")
    private String commentContent;

    private Board board;
    private static ModelMapper modelMapper = new ModelMapper();

    public Geniecomment createComment(){
        return modelMapper.map(this, Geniecomment.class);
    }

    public static CommentFormDto of(Geniecomment geniecomment) {
        return modelMapper.map(geniecomment, CommentFormDto.class);
    }

}
