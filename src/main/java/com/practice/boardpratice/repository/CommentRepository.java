package com.practice.boardpratice.repository;

import com.practice.boardpratice.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from Comment c where c.board_id = :bno", nativeQuery = true)
    List<Comment> findByBoardId(@Param("bno") long bno);
}
