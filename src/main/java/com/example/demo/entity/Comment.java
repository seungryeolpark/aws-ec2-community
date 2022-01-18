package com.example.demo.entity;

import com.example.demo.entity.enums.DeleteStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private Long groupId;

    @Max(value = 8)
    private Long depth;

    @NotEmpty
    private String nickname;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus deleteStatus;

    @NotEmpty
    @Lob
    private String content;
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(Long id, Long groupId, String nickname, String password, DeleteStatus deleteStatus, String content,
                   LocalDateTime createTime, Member member, Board board, Comment parentComment, Long depth) {
        this.id = id;
        this.groupId = groupId;
        this.nickname = nickname;
        this.password = password;
        this.deleteStatus = deleteStatus;
        this.content = content;
        this.createTime = createTime;
        this.member = member;
        this.board = board;
        this.parentComment = parentComment;
        this.depth = depth;
    }

    //값 변경
    public void setGroupId(Long id) {
        this.groupId = id;
    }

    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setContent(String content) {
        this.content = content;
    }
}