package com.bloom.pium.data.dto;

import com.bloom.pium.data.entity.Board;
import com.bloom.pium.data.entity.UserInfo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MatchingResponseDto {

    private Long matchingId;
    private String title;
    private String comment;

//    private boolean participate;

    private UserInfo userId;
    private Board boardId;


}
