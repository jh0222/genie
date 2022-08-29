package com.genie.entity;

import com.genie.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity{
    @Id
    @Column(name = "board_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column(nullable = true)
    private String boardTitle;

    @Lob
    @Column(nullable = true)
    private String boardContent;

    private int boardCount;

    private int commentStatus;

    public static Board createBoard(BoardFormDto boardFormDto, Member member){
        Board board = new Board();
        board.setMember(member);
        board.setBoardTitle(boardFormDto.getBoardTitle());
        board.setBoardContent(boardFormDto.getBoardContent());
        board.setBoardCount(0);
        board.setCommentStatus(0);
        return board;
    }

    public void updateBoard(BoardFormDto boardFormDto){
        this.boardTitle = boardFormDto.getBoardTitle();
        this.boardContent = boardFormDto.getBoardContent();
    }

}
