package com.bloom.pium.data.dto;

import com.bloom.pium.data.entity.Board;
import com.bloom.pium.data.entity.UserInfo;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MatchingDto {

    private Long matchingId;    // 매칭 고유 번호

    private String title;

    private String comment;

    private boolean participate;    // 참여 결정 여부

    private UserInfo userId;

    private Board boardId;


}
