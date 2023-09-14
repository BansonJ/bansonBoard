package com.practice.boardpratice;

import com.practice.boardpratice.domain.Board;
import com.practice.boardpratice.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@SpringBootConfiguration
public class MainTest {

    @Autowired
    private final BoardService boardService;

    public MainTest(BoardService boardService) {
        this.boardService = boardService;
    }

    @Test
    void board(){

    }

}
