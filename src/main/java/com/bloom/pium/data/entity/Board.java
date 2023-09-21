package com.bloom.pium.data.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public class Board extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")    // 운동장비 추천 게시물 매니투원
    private Category category;



}


