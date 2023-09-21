package com.bloom.pium.service.impl;

import com.bloom.pium.data.dto.MessageDto;
import com.bloom.pium.data.entity.Message;
import com.bloom.pium.data.entity.UserInfo;
import com.bloom.pium.data.repository.MessageRepository;
import com.bloom.pium.data.repository.UserInfoRepository;
import com.bloom.pium.data.dto.MessageResponseDto;
import com.bloom.pium.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;
    private UserInfoRepository userInfoRepository;

    @Autowired
    MessageServiceImpl(MessageRepository messageRepository, UserInfoRepository userInfoRepository){
        this.messageRepository = messageRepository;
        this.userInfoRepository = userInfoRepository;
    }


    @Override
    public MessageDto writeMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setMessageTitle(messageDto.getMessageTitle());
        message.setContent(messageDto.getContent());

        UserInfo receiver = userInfoRepository.findById(messageDto.getReceiveUserName())
                .orElseThrow(() -> new RuntimeException("receiver not found"));
        UserInfo sender = userInfoRepository.findById(messageDto.getSendUserName())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        message.setSender(sender);
        message.setRecipient(receiver);
        message.setCreatedDate(LocalDateTime.now());
        message.setCheckStatus(false);
        messageRepository.save(message);

        // 반환용 builder 패턴
        MessageDto messageSave = MessageDto.builder()
                .messageTitle(message.getMessageTitle())
                .content(message.getContent())
                .receiveUserName(message.getRecipient().getUserId())
                .sendUserName(message.getSender().getUserId())
                .checkStatus(message.isCheckStatus())
                .build();

        return messageSave;
    }


    @Override
    public List<MessageResponseDto> getSentMessagesByUserId(Long userId) {
        List<Message> sentMessages = messageRepository.findBySenderUserId(userId);
        return convertToResponseDtoList(sentMessages);
    }

    // convertToResponseDtoList 메서드 구현

    private List<MessageResponseDto> convertToResponseDtoList(List<Message> messages) {
        return messages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private MessageResponseDto convertToResponseDto(Message message) {
        MessageResponseDto responseDto = new MessageResponseDto();
        responseDto.setMessageId(message.getMessageId());
        responseDto.setMessageTitle(message.getMessageTitle());
        responseDto.setMessageContent(message.getContent());
        responseDto.setCreatedDate(message.getCreatedDate());
        return responseDto;
    }

    // 메세지 불러오기
    @Override
    public MessageDto getMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        return mapToDto(message);
    }

    @Override
    public MessageDto readMessageStatus(Long messageId) {
        messageRepository.findById(messageId).orElseThrow(RuntimeException::new);

        Message message = messageRepository.findById(messageId).get();
        message.setCheckStatus(true);
        messageRepository.save(message);

        // 반환용 builder 패턴
        MessageDto messageSave = MessageDto.builder()
                .messageTitle(message.getMessageTitle())
                .content(message.getContent())
                .receiveUserName(message.getRecipient().getUserId())
                .sendUserName(message.getSender().getUserId())
                .checkStatus(message.isCheckStatus())
                .build();

        return messageSave;

    }

    @Override
    public int getUnreadMessageCount(Long recipient) {
        UserInfo recipientUser = userInfoRepository.findById(recipient).get();
        int countUnRead= messageRepository.countByRecipientAndCheckStatus(recipientUser,false);
        return countUnRead;
    }

    @Override
    public List<MessageDto> getMessageByUsername(String username) {
        UserInfo receiver = userInfoRepository.findByUsername(username);

        if (receiver != null) {
            List<Message> messages = messageRepository.findByRecipientUsername(username);
            return messages.stream()
                    .map(message -> {
                        MessageDto messageDto = mapToDto(message);
                        messageDto.setSenderUsername(message.getSender().getUsername());
                        messageDto.setReceiverUsername(message.getRecipient().getUsername());
                        return messageDto;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Receiver not found");
        }
    }


    // 메세지 삭제
    @Override
    public void deleteMessageById(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    private MessageDto mapToDto(Message message) {
        return MessageDto.builder()
                .messageId(message.getMessageId())
                .sendUserName(message.getSender().getUserId())
                .receiveUserName(message.getRecipient().getUserId())
                .messageTitle(message.getMessageTitle())
                .content(message.getContent())
                .createdDate(message.getCreatedDate())
                .build();
    }



}
