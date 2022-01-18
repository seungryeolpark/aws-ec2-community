package com.example.demo.entity;

import com.example.demo.entity.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
    private List<Member_Authority> memberAuthorities = new ArrayList<>();

    @Builder
    public Authority(Long id, Role role) {
        this.id = id;
        this.role = role;
    }
}
