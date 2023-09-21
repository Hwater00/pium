package com.bloom.pium.service;

import com.bloom.pium.data.dto.MessageDto;
import com.bloom.pium.data.dto.MessageResponseDto;

import java.util.List;

public interface MessageService {
    // 쪽지 작성
    MessageDto writeMessage(MessageDto messageDto);
    // 보낸 쪽지 조회

    // 유저 한명이 받은 메세지 전체 불러오기
    List<MessageDto> getMessageByUsername(String username);

    // 쪽지 내용 확인(상세보기) -> 읽음에 따른 상태값 변경 -> 주고 받은 내용?
    MessageDto getMessageById(Long messageId);

    // 메세지 읽음에 따른 상태값 변경
    MessageDto readMessageStatus(Long messageId);

    // 읽지 않은 쪽지 갯수 조회 = 알림
    int getUnreadMessageCount(Long recipient);

    // 메세지 삭제
    void deleteMessageById(Long messageId);

    // 유저 한 명이 보낸 메세지
    List<MessageResponseDto> getSentMessagesByUserId(Long userId);
}
