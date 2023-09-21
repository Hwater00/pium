package com.bloom.pium.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name="boardlike")
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private BoardMatching boardMatching;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserInfo userInfo;

    private boolean liked; // 좋아요 여부 (true: 추천, false: 추천 취소)
}