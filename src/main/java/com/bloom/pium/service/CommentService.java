package com.bloom.pium.service;

import com.bloom.pium.data.dto.CommentDto;
import com.bloom.pium.data.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {


    // 게시글 댓글 수정
    CommentResponseDto modifyComment(Long commentId, String content) throws Exception;


    // 게시글 댓글 작성 -> // 게시글 대댓글 작성
    CommentDto writeComment(CommentDto commentDto);


   // CommentResponseDto saveComment(CommentDto commentDto); // 저장


    // 게시글 대댓글 삭제 - 전체삭제? 삭제 후 남음?
    void DeleteCToComment(Long commentId);

    // 댓글 조회 -> 댓글 정렬

    List<CommentResponseDto> getCommentsByBoardId(Long boardId);
    List<CommentResponseDto> getCommentsByUserId(Long userId);
    // ↑↑ 추가 (2023.09.16.토)
}

