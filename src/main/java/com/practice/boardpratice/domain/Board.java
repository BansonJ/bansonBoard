package com.practice.boardpratice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String word;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
