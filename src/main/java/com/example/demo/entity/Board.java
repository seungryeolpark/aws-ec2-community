package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @NotEmpty
    private String title;

    private String email;

    @NotEmpty
    private String uuid;

    @NotEmpty
    private String nickname;

    private String password;

    @NotEmpty
    @Lob
    private String content;

    private LocalDateTime createTime;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Image> imageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(Long id, String email, String uuid, String title, String nickname, String password, String content, LocalDateTime createTime, Member member) {
        this.id = id;
        this.email = email;
        this.uuid = uuid;
        this.title = title;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.createTime = createTime;
        this.member = member;
    }

    //변경
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
