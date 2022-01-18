package com.example.demo.entity;

import com.example.demo.entity.enums.DeleteStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @NotEmpty
    private String uuid;

    private String url;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Image(Long id, String uuid, String url, DeleteStatus deleteStatus, Board board) {
        this.id = id;
        this.uuid = uuid;
        this.url = url;
        this.deleteStatus = deleteStatus;
        this.board = board;
    }

    //변경
    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
