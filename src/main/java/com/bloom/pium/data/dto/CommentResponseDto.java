package com.bloom.pium.data.dto;


import com.bloom.pium.data.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class CommentResponseDto {
    private  Long commentId;
    private String content;
    private Long pContentId;
//    private Long cContentId;
    private Long boardId;
    private Long userId;

    private String username;

    private LocalDateTime createdDate;

    private LocalDateTime ModifiedDate;



}
