package com.bloom.pium.controller;

import com.bloom.pium.data.dto.CommentDto;
import com.bloom.pium.data.dto.CommentResponseDto;
import com.bloom.pium.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {
    CommentService commentService;

    @Autowired
    CommentController(CommentService commentService){
        this.commentService =commentService;
    }

    @PostMapping("/write")
    @ApiOperation(value = "댓글 작성")
    public ResponseEntity<CommentDto> CommentWrite( CommentDto CommentDto){
        commentService.writeComment(CommentDto);
        return ResponseEntity.status(HttpStatus.OK).body(CommentDto);
    }

    @PutMapping("/modify")
    public  ResponseEntity<CommentResponseDto> UpdateComment(Long commentId , String content) throws Exception {
        CommentResponseDto comment =  commentService.modifyComment(commentId, content);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }


    @GetMapping("/byBoard/{boardId}")
    @ApiOperation(value = "게시글ID로 댓글 불러오기")
    public String getCommentsByBoardId(@PathVariable Long boardId, Model model) {
        List<CommentResponseDto> comments = commentService.getCommentsByBoardId(boardId);
        model.addAttribute("comments", comments);
        return "comment"; // comment.html 템플릿을 렌더링
    }

    @GetMapping("/byUser/{userId}")
    @ApiOperation(value = "유저ID로 댓글 불러오기")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByUserId(@PathVariable Long userId) {
        List<CommentResponseDto> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
    // ↑↑ 추가 (2023.09.16.토)
}

