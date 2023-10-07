package com.practice.boardpratice.service;

import com.practice.boardpratice.domain.Board;
import com.practice.boardpratice.domain.Comment;
import com.practice.boardpratice.domain.Member;
import com.practice.boardpratice.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAllCommentByBoardId(long bno) {
        return commentRepository.findByBoardId(bno);
    }

    public void saveComment(String comment, Member member, Board board) {
        Comment comments = Comment.builder().
                comment(comment)
                .member(member)
                .board(board)
                .createDate(LocalDate.now())
                .build();

        commentRepository.save(comments);
    }

    public void deleteComment(long cno) {
        commentRepository.deleteById(cno);
    }
}
