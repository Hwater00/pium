package com.bloom.pium.data.dto;

import com.bloom.pium.data.entity.Board;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardResponseDto extends Board {

    private Long boardId;
    private String title;
    private String content;
    private int likeCnt;
    private int viewCnt;

    private String categoryName;
    private String username;
    private int commentCount;


}

//ProductResponseDto: 서버에서 클라이언트
