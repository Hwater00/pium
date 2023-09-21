package com.bloom.pium.data.entity;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name="message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String messageTitle;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private UserInfo sender;    // 보낸사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient", nullable = false)
    private UserInfo recipient;  // 받는사람

    @Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
    private boolean checkStatus;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdDate; // 댓글 작성일

}

