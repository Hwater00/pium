package com.bloom.pium.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name="boardMatching")
public class BoardMatching extends Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private  String schedule;

    @Column(nullable = false)
    private String place;

    @Column(nullable = true)
    private int likeCnt;

    @OneToMany(mappedBy = "boardMatching", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(unique = false)
    private List<BoardLike> boardLikes;


    @Column(nullable = true)
    private int viewCnt;

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private UserInfo userInfo;

    @OneToMany(mappedBy = "boardMatching", fetch =FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(unique = false)
    private List<Comment> comment;

    @Column(nullable = true)
    private int commentCount;

    @ManyToOne
    @JoinColumn(name = "category_id") // 카테고리와 연결할 외래 키
    private Category category;

    // 매칭 마감 관련 (미해결)
    @Column(nullable = true)
    private boolean deadline;
}
