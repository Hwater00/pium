package com.bloom.pium.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name="matching")
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;    // 매칭 고유 번호

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private boolean participate;    // 참여 결정 여부

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private UserInfo userId;

    @ManyToOne
    @JoinColumn(name = "boardId")
    @ToString.Exclude
    private BoardMatching boardId;

}
