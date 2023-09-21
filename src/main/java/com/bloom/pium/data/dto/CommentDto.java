package com.bloom.pium.data.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentDto {

    private String content;
    private Long pContentId;
    private Long boardId;
    private Long userId;

    private LocalDateTime createdDate;



}
