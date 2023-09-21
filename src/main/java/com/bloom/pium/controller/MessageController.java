package com.bloom.pium.controller;

import com.bloom.pium.data.dto.MessageDto;
import com.bloom.pium.data.dto.MessageResponseDto;
import com.bloom.pium.service.MessageService;
import com.bloom.pium.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    MessageController(MessageService messageService){this.messageService = messageService;}

    @PostMapping("/send")
    public ResponseEntity<MessageDto> sendMessage(MessageDto messageDto){
        MessageDto message = messageService.writeMessage(messageDto);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }



    // 유저 한명이 받은 메세지 전체 불러오기
    @GetMapping("/receiver/{username}")
    public ResponseEntity<List<MessageDto>> getMessageByUsername(@PathVariable String username) {
        List<MessageDto> messages = messageService.getMessageByUsername(username);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessageResponseDto>> getSentMessagesByUserId(@PathVariable Long userId) {
        List<MessageResponseDto> sentMessages = messageService.getSentMessagesByUserId(userId);
        return new ResponseEntity<>(sentMessages, HttpStatus.OK);
    }

    // 메세지 불러오기
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long messageId) {
        MessageDto message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }
    // 위 아래 병합?
    @GetMapping("/read/{messageId}")
    public ResponseEntity<MessageDto> readMessage(@PathVariable Long messageId){
        MessageDto message = messageService.readMessageStatus(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/listCount/{userId}")
    public ResponseEntity<Integer> unReadMessage(@PathVariable Long userId) {
        Integer unreadMessageCount = messageService.getUnreadMessageCount(userId);

        //if (unreadMessageCount != null) {
            return ResponseEntity.ok(unreadMessageCount);

//        else {
//            return ResponseEntity.notFound().build();
//        }
    }

    // 메세지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long messageId) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity.noContent().build();
    }


}
