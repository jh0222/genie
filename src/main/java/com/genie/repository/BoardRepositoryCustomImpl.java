package com.genie.repository;

import com.genie.dto.BoardSearchDto;
import com.genie.entity.Board;
import com.genie.entity.QBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("boardTitle", searchBy)){
            return QBoard.board.boardTitle.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("memberId", searchBy)){
            return QBoard.board.member.memberId.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QueryResults<Board> results = queryFactory
                .selectFrom(QBoard.board)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Board> getBoardPage_count(BoardSearchDto boardSearchDto, Pageable pageable){
        QueryResults<Board> results = queryFactory
                .selectFrom(QBoard.board)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.boardCount.desc())
                .orderBy(QBoard.board.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Board> getBoardPage_commentno(BoardSearchDto boardSearchDto, Pageable pageable){
        QueryResults<Board> results = queryFactory
                .selectFrom(QBoard.board)
                .where(QBoard.board.commentStatus.eq(0))
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.createdTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Board> getBoardPage_commentyes(BoardSearchDto boardSearchDto, Pageable pageable){
        QueryResults<Board> results = queryFactory
                .selectFrom(QBoard.board)
                .where(QBoard.board.commentStatus.eq(1))
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.createdTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}
