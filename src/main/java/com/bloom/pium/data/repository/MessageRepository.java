package com.bloom.pium.data.repository;

import com.bloom.pium.data.entity.Message;
import com.bloom.pium.data.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    int countByRecipientAndCheckStatus(UserInfo recipient , boolean checkStatus);
    List<Message> findByRecipientUsername(String username);
//
    List<Message> findBySenderUserId(Long senderId);

}
