package com.bloom.pium.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "category")
public class Category {
    // ↓↓ 추가 (2023.09.17.일)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<BoardMatching> boardMatching;

}

