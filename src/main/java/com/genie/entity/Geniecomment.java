package com.genie.entity;

import com.genie.dto.CommentFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "geniecomment")
@Getter
@Setter
@ToString
public class Geniecomment extends BaseEntity{
    @Id
    @Column(name = "comment_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    private Board board;

    @Lob
    @Column(nullable = true)
    private String commentContent;

    public static Geniecomment createComment(CommentFormDto commentFormDto, Board board){
        Geniecomment geniecomment = new Geniecomment();
        geniecomment.setBoard(board);
        geniecomment.setCommentContent(commentFormDto.getCommentContent());
        return geniecomment;
    }
}
