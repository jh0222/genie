package com.genie.repository;

import com.genie.entity.Board;
import com.genie.entity.Geniecomment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Geniecomment, Long> {
    Geniecomment findByBoard(Board board);
}
