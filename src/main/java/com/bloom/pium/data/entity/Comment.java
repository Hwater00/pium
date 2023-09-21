package com.bloom.pium.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name="Comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name ="boardId")
    private BoardMatching boardMatching;

    @ManyToOne
    @JoinColumn(name = "parentComment", nullable = true)
    private Comment pComment;

    @OneToMany(mappedBy = "pComment",fetch = FetchType.LAZY)
    private List<Comment> cComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserInfo userInfo;

}
