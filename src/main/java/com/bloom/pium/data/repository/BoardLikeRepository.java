package com.bloom.pium.data.repository;

import com.bloom.pium.data.entity.BoardLike;
import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    // 유저(userId)와 게시판(boardId)에 대한 좋아요 정보를 찾는 메서드
    BoardLike findByUserInfoAndBoardMatching(UserInfo userInfo, BoardMatching boardMatching);
}
