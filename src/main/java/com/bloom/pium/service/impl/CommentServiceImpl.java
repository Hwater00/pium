package com.bloom.pium.service.impl;

import com.bloom.pium.data.dto.CommentDto;
import com.bloom.pium.data.dto.CommentResponseDto;
import com.bloom.pium.data.entity.Comment;
import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.UserInfo;
import com.bloom.pium.data.repository.BoardRepository;
import com.bloom.pium.data.repository.CommentRepository;
import com.bloom.pium.data.repository.UserInfoRepository;
import com.bloom.pium.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private BoardRepository boardRepository;
    private UserInfoRepository userInfoRepository;
    @Autowired
    CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository, UserInfoRepository userInfoRepository){
        this.commentRepository =commentRepository;
        this.boardRepository =boardRepository;
        this.userInfoRepository = userInfoRepository;
    }


    @Override
    public CommentDto writeComment(CommentDto commentDto) {
        //게시글이 없으면
        boardRepository.findById(commentDto.getBoardId()).orElseThrow(RuntimeException::new);

        // 자식 X
        if(commentDto.getPContentId() == null) {
            Comment comment = new Comment();
            comment.setContent(commentDto.getContent());
            comment.setBoardMatching(boardRepository.findById(commentDto.getBoardId()).get());
            comment.setUserInfo(userInfoRepository.findById(commentDto.getUserId()).get());
            comment.setCreatedDate(LocalDateTime.now());
            comment.setModifiedDate(LocalDateTime.now());

            commentRepository.save(comment);
        } else {

            // 자식 저장
            Comment commentC = new Comment();
            commentC.setContent(commentDto.getContent());
            commentC.setBoardMatching(boardRepository.findById(commentDto.getBoardId()).get());
            commentC.setUserInfo(userInfoRepository.findById(commentDto.getUserId()).get());
            commentC.setPComment(commentRepository.findById(commentDto.getPContentId()).get());
            commentC.setCreatedDate(LocalDateTime.now());
//            commentC.setModifiedDate(LocalDateTime.now()); // 댓글 작성에는 필요 없음
            BoardMatching boardMatching = boardRepository.findById(commentDto.getBoardId())
                    .orElseThrow(() -> new RuntimeException("BoardMatching not found with id: " + commentDto.getBoardId()));

            // Update the commentCount and save the BoardMatching entity
            boardMatching.setCommentCount(boardMatching.getCommentCount() + 1);
            boardRepository.save(boardMatching);
            commentRepository.save(commentC);

            // 부모의 자식 업데이트?

        }
        return commentDto;
    }

    @Override
    public void DeleteCToComment(Long commentId) {

    }

    @Override
    public CommentResponseDto modifyComment(Long commentId, String content) throws Exception {
        Comment foundComment = commentRepository.findById(commentId).get();
        foundComment.setContent(content);
        // 모든 필드 값을 다 수정해야 하는가?  //entity.update(params.getTitle(), params.getContent(), params.getWriter());?

        Comment changedComment = commentRepository.save(foundComment);
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setCommentId(changedComment.getCommentId());
        commentResponseDto.setContent(changedComment.getContent());

        return commentResponseDto;

    }


    // 게시글 불러오기 전 셋팅용
    private CommentResponseDto convertToCommentDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
//        commentResponseDto.setCommentId(comment.getCommentId()); // 코멘트 고유번호까지는 필요 없을거 같음
        commentResponseDto.setContent(comment.getContent());
        commentResponseDto.setUserId(comment.getUserInfo().getUserId()); // ID랑 name중에 하나만 써도 무방함.
        UserInfo userInfo = comment.getUserInfo();
        if (userInfo != null) {
            commentResponseDto.setUsername(userInfo.getUsername());
        }
        commentResponseDto.setBoardId(comment.getBoardMatching().getBoardId());
        commentResponseDto.setCreatedDate(comment.getCreatedDate());
        commentResponseDto.setModifiedDate(comment.getModifiedDate());
        return commentResponseDto;
    }


    public List<CommentResponseDto> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardMatching_BoardId(boardId);
        return comments.stream()
                .map(this::convertToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserInfo_UserId(userId);
        return comments.stream()
                .map(this::convertToCommentDto)
                .collect(Collectors.toList());
    }

}
