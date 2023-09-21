package com.bloom.pium.controller;

import com.bloom.pium.data.dto.BoardDto;
import com.bloom.pium.data.dto.BoardResponseDto;
import com.bloom.pium.data.dto.CommentResponseDto;
import com.bloom.pium.service.BoardService;
import com.bloom.pium.service.CategoryService;
import com.bloom.pium.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bloom.pium.data.dto.ModifyBoardDto;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private  final  CategoryService categoryService;

    @Autowired
    public BoardController(BoardService boardService,CommentService commentService,CategoryService categoryService) {
        this.boardService = boardService;
        this.categoryService=categoryService;
        this.commentService =commentService;
    }

    @GetMapping("/form")
    public String goToBoard() {
        return "BoardForm";
    }


    @GetMapping()
    public ResponseEntity<BoardResponseDto> getBoard(Long boardId){
        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }




    @PostMapping("/write")
    @ApiOperation(value = "게시글 작성")
    public  ResponseEntity<BoardResponseDto> createdBoard(@RequestBody BoardDto boardDto){
        BoardResponseDto boardResponseDto = boardService.saveBoard(boardDto);
        System.out.println(boardResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }


    @PutMapping("/modify")
    public ResponseEntity<BoardResponseDto> modifyBoard(
            @RequestBody ModifyBoardDto modifyBoard) throws Exception {
        BoardResponseDto boardResponseDto = boardService.modifyBoard(
                modifyBoard.getBoardId(),
                modifyBoard.getTitle(),
                modifyBoard.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }


    @DeleteMapping("/delete")
    @ApiOperation(value = "게시글 삭제")
    public ResponseEntity <String> deleteBoard (Long boardId) throws Exception{
        boardService.deleteBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다");
    }


    @GetMapping("/paging")
    @ApiOperation(value = "페이징")
    public Page<BoardResponseDto> getAllBoards(
            @RequestParam(defaultValue = "1") int page  // ,@RequestParam(defaultValue = "10") int size
    ) {
        return boardService.getAllBoards(page);
    }

    @PostMapping("/{boardId}/like/{userId}")
    @ApiOperation(value = "좋아요")
    public ResponseEntity<BoardResponseDto> toggleLike(
            @PathVariable Long boardId,
            @PathVariable Long userId
    ) {
        BoardResponseDto updatedBoard = boardService.toggleLike(boardId, userId);
        return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
    }



    @GetMapping("/{boardId}")
    @ApiOperation(value = "n번 게시글 내용 + 댓글")
    public ModelAndView getBoardDetail(@PathVariable Long boardId, Model model) {
        BoardResponseDto board = boardService.getBoardById(boardId);
        ModelAndView modelAndView = new ModelAndView("boardDetail"); // Thymeleaf 템플릿의 경로
        modelAndView.addObject("board", board);
        List<CommentResponseDto> comments = commentService.getCommentsByBoardId(boardId);
        model.addAttribute("comments", comments);
        System.out.println(board);
        return modelAndView;
    }

}

