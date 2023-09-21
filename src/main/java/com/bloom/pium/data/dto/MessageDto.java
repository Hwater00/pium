package com.bloom.pium.data.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MessageDto {

    private Long messageId;
    private String messageTitle;
    private String content;
    private Long receiveUserName;
    private Long sendUserName;

    private boolean checkStatus; //(true: 읽음, false: 안 읽음)

    private LocalDateTime createdDate;

    private String senderUsername; //
    private String receiverUsername; //

}
