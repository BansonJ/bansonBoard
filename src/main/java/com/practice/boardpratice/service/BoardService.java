package com.practice.boardpratice.service;

import com.practice.boardpratice.domain.Board;
import com.practice.boardpratice.domain.Member;
import com.practice.boardpratice.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<Board> getAllBoard(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return boardRepository.findAll(pageable);
    }

    public Page<Board> findByBoardName(String search, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return boardRepository.findByTitleContaining(search, pageable);
    }

    public Board createBoard(String title, String word, Member member) throws IOException {
        Board board = Board.builder().
                title(title).
                word(word).
                member(member).
                createDate(LocalDate.now()).
                build();
        return boardRepository.save(board);
    }

    public Optional<Board> findById(Long bno) {
        return boardRepository.findById(bno);
    }

    public Board modifyBoard(long bno, String title, String word) throws IOException {
        Board board = this.findById(bno).orElseThrow();
        board.setTitle(title);
        board.setWord(word);
        return boardRepository.save(board);
    }

    public void deleteBoard(long bno) {
        boardRepository.deleteById(bno);
    }
}
