package com.bloom.pium.data.repository;

import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class BoardMatchingRepositoryTest {
    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    BoardRepository boardRepository;


    @Test
    void relationshipTest1(){ // UserInfo - Board : ManyToOne
        UserInfo user = new UserInfo();
        user.setUsername("2023"); // 아이디 생성
        user.setPassword("0000");
        user.setName("어른이");
        user.setPhone("010-1234-5678");
        user.setGender("여성");
        userInfoRepository.save(user);

        BoardMatching boardMatching = new BoardMatching();
        boardMatching.setTitle("오운완 1일차");
        boardMatching.setContent("유산소+근력");
        boardMatching.setSchedule("토요일");
        boardMatching.setPlace("하나누리");
        boardMatching.setUserInfo(user);
        boardRepository.save(boardMatching);

        // TEST
        System.out.println("게사판:"+boardRepository.findById(1L).orElseThrow(RuntimeException::new));

        System.out.println("글 작성자:"+ userInfoRepository.findById(1L).orElseThrow(RuntimeException::new).getUserId());
    }
}
