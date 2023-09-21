package com.bloom.pium.service.impl;

import com.bloom.pium.data.dto.BoardDto;
import com.bloom.pium.data.dto.BoardResponseDto;
import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.BoardLike;
import com.bloom.pium.data.entity.Category;
import com.bloom.pium.data.entity.UserInfo;
import com.bloom.pium.data.repository.BoardLikeRepository;
import com.bloom.pium.data.repository.BoardRepository;
import com.bloom.pium.data.repository.CategoryRepository;
import com.bloom.pium.data.repository.UserInfoRepository;
import com.bloom.pium.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;
    private UserInfoRepository userInfoRepository;
    private BoardLikeRepository boardLikeRepository;
    private final CategoryRepository categoryRepository;
    private final static int size =10;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository,
                            UserInfoRepository userInfoRepository,
                            BoardLikeRepository boardLikeRepository,
                            CategoryRepository categoryRepository) {
        this.boardRepository = boardRepository;
        this.userInfoRepository = userInfoRepository;
        this.boardLikeRepository = boardLikeRepository;
        this.categoryRepository = categoryRepository;
    }

    // 조회
    @Override
    public BoardResponseDto getBoard(Long boardId) {
        BoardMatching boardMatching = boardRepository.findById(boardId).orElse(null);

        if (boardMatching != null) {
            BoardResponseDto boardResponseDto = new BoardResponseDto();
            boardResponseDto.setBoardId(boardMatching.getBoardId());
            boardResponseDto.setTitle(boardMatching.getTitle());
            boardResponseDto.setContent(boardMatching.getContent());
            boardResponseDto.setViewCnt(boardMatching.getViewCnt());
            boardResponseDto.setLikeCnt(boardMatching.getLikeCnt());
            boardResponseDto.setCreatedDate(boardMatching.getCreatedDate());
            boardResponseDto.setModifiedDate(boardMatching.getModifiedDate());

            int commentCount = boardMatching.getCommentCount();
            boardResponseDto.setCommentCount(commentCount);

            // Fetch the category name from the board's category
            String categoryName = boardMatching.getCategory() != null ? boardMatching.getCategory().getName() : null;
            boardResponseDto.setCategoryName(categoryName);

            // Fetch the username from the associated UserInfo
            UserInfo userInfo = boardMatching.getUserInfo();
            if (userInfo != null) {
                boardResponseDto.setUsername(userInfo.getUsername());
            }
            return boardResponseDto;
        } else {
            return null; // Handle the case where the board is not found
        }
    }


    @Override
    public BoardResponseDto saveBoard(BoardDto boardDto) {
        BoardMatching boardMatching = new BoardMatching();
        boardMatching.setTitle(boardDto.getTitle());
        boardMatching.setContent(boardDto.getContent());
        boardMatching.setSchedule(boardDto.getSchedule());
        boardMatching.setPlace(boardDto.getPlace());
        boardMatching.setViewCnt(0); // 초기값으로 0 설정
        boardMatching.setLikeCnt(0);
        boardMatching.setCreatedDate(LocalDateTime.now());
        boardMatching.setModifiedDate(LocalDateTime.now());
        boardMatching.setCommentCount(0);
        // ↓↓ 추가 (2023.09.17.일)
        boardMatching.setCreatedDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        // categoryId를 이용하여 카테고리 엔티티를 가져옴
        Category category = categoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));

        // BoardMatching 엔티티에 카테고리 정보를 설정
        boardMatching.setCategory(category);

        // userId를 이용하여 유저 이름 엔티티를 가져옴
        UserInfo userInfo = userInfoRepository.findById(boardDto.getUserInfoId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        // BoardMatching 엔티티에 유저 이름 정보를 설정
        boardMatching.setUserInfo(userInfo);
        // ↑↑ 추가 (2023.09.17.일)

        BoardMatching savedBoardMatching = boardRepository.save(boardMatching);

        BoardResponseDto boardResponseDto = new BoardResponseDto();
        boardResponseDto.setBoardId(savedBoardMatching.getBoardId());
        boardResponseDto.setTitle(savedBoardMatching.getTitle());
        boardResponseDto.setContent(savedBoardMatching.getContent());


        boardResponseDto.setCreatedDate(LocalDateTime.now());
        String categoryName = boardMatching.getCategory() != null ? boardMatching.getCategory().getName() : null;
        boardResponseDto.setCategoryName(categoryName);
        String userName = boardMatching.getUserInfo() != null ? boardMatching.getUserInfo().getUsername() : null;
        boardResponseDto.setUsername(userName);

        return boardResponseDto;
    }

    @Override
    public BoardResponseDto modifyBoard(Long boardId, String title, String content) throws Exception {
        BoardMatching foundBoard = boardRepository.findById(boardId).get();
        foundBoard.setTitle(title);
        foundBoard.setContent(content);

        BoardMatching changedBoard = boardRepository.save(foundBoard);

        BoardResponseDto boardResponseDto = new BoardResponseDto();
        boardResponseDto.setBoardId(changedBoard.getBoardId());
        boardResponseDto.setTitle(changedBoard.getTitle());
        boardResponseDto.setContent(changedBoard.getContent());

        return boardResponseDto;
    }

    @Override
    public void deleteBoard(Long number) throws Exception{
        Optional<BoardMatching> selectedBoard = boardRepository.findById(number);

        if(selectedBoard.isPresent()){
            BoardMatching board = selectedBoard.get();

            boardRepository.delete(board);

        } else{
            throw new Exception();
        }
    }

    // 게시글 전체 불러오기
    @Override
    public Page<BoardResponseDto> getAllBoards(int page) {
        // 페이지와 페이지 크기를 기반으로 페이징 요청
        Pageable pageable = PageRequest.of(page-1, size); // 1페이지부터 시작

        // 게시물을 페이징하여 가져옴
        Page<BoardMatching> boardPage = boardRepository.findAll(pageable);

        // Board엔티티를 BoardResponseDto로 변환하여 반환
        return boardPage.map(this::convertToDto);
    }

    // Board 엔티티를 BoardResponseDto로 변환하는 메서드
    private BoardResponseDto convertToDto(BoardMatching board) {

            BoardResponseDto boardResponseDto = new BoardResponseDto();
            boardResponseDto.setBoardId(board.getBoardId());
            boardResponseDto.setTitle(board.getTitle());
            boardResponseDto.setContent(board.getContent());
            boardResponseDto.setViewCnt(board.getViewCnt());
            boardResponseDto.setLikeCnt(board.getLikeCnt());

            boardResponseDto.setCreatedDate(board.getCreatedDate());
            boardResponseDto.setModifiedDate(board.getModifiedDate());
            boardResponseDto.setCommentCount(board.getCommentCount());

            // Fetch the category name from the board's category
            String categoryName = board.getCategory() != null ? board.getCategory().getName() : null;
            boardResponseDto.setCategoryName(categoryName);

            // Fetch the username from the associated UserInfo
            UserInfo userInfo = board.getUserInfo();
            if (userInfo != null) {
                boardResponseDto.setUsername(userInfo.getUsername());
            }
            return boardResponseDto;

    }

    // 좋아요
    @Override
    public BoardResponseDto toggleLike(Long boardId, Long userId) {
        // 게시판(BoardMatching)과 유저(UserInfo)를 가져옵니다.
        BoardMatching boardMatching = boardRepository.findById(boardId).orElse(null);
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);

        if (boardMatching != null && userInfo != null) {
            // 좋아요 정보를 가져옵니다.
            BoardLike boardLike = boardLikeRepository.findByUserInfoAndBoardMatching(userInfo, boardMatching);

            if (boardLike == null) {
                // 좋아요 정보가 없으면 새로 생성하고 추가
                boardLike = BoardLike.builder()
                        .boardMatching(boardMatching)
                        .userInfo(userInfo)
                        .liked(true)
                        .build();
                boardMatching.setLikeCnt(boardMatching.getLikeCnt() + 1);
            } else {
                // 좋아요 정보가 있으면 상태를 반전시킵니다.
                boardLike.setLiked(!boardLike.isLiked());
                if (boardLike.isLiked()) {
                    boardMatching.setLikeCnt(boardMatching.getLikeCnt() + 1);
                } else {
                    boardMatching.setLikeCnt(boardMatching.getLikeCnt() - 1);
                }
            }

            // 변경된 정보를 저장합니다.
            boardRepository.save(boardMatching);
            boardLikeRepository.save(boardLike);

            // 변경된 게시판 정보를 응답합니다.
            BoardResponseDto boardResponseDto = new BoardResponseDto();
            boardResponseDto.setBoardId(boardMatching.getBoardId());
            boardResponseDto.setTitle(boardMatching.getTitle());
            boardResponseDto.setContent(boardMatching.getContent());
            boardResponseDto.setLikeCnt(boardMatching.getLikeCnt());
            boardResponseDto.setViewCnt(boardMatching.getViewCnt());
            // ↓↓ 추가 (2023.09.17.일)
            boardResponseDto.setCreatedDate(boardMatching.getCreatedDate());
            boardResponseDto.setModifiedDate(boardMatching.getModifiedDate());
            boardResponseDto.setCommentCount(boardMatching.getCommentCount());
            // Fetch the category name from the board's category
            String categoryName = boardMatching.getCategory() != null ? boardMatching.getCategory().getName() : null;
            boardResponseDto.setCategoryName(categoryName);

            // Fetch the username from the associated UserInfo
            boardMatching.getUserInfo();
            if (userInfo != null) {
                boardResponseDto.setUsername(userInfo.getUsername());
            }
            // ↑↑ 추가 (2023.09.17.일)
            return boardResponseDto;
        }

        return null;
    }


    @Override
    public List<BoardResponseDto> getBoardMatchingByCategory(Long categoryId) {
        // 카테고리를 조회합니다.
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        // 해당 카테고리에 속한 게시글 목록을 가져옵니다.
        List<BoardMatching> boardMatching = boardRepository.findByCategory(category);

        // 가져온 게시글 목록을 Dto로 변환하여 반환합니다.
        return boardMatching.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public BoardResponseDto getBoardById(Long boardId) {
        BoardMatching boardMatching = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found with id: " + boardId));

        // 여기에서 BoardMatching 엔티티를 BoardResponseDto로 변환하여 리턴하는 로직을 추가해야 합니다.
        // BoardResponseDto에는 게시글의 상세 정보를 담아야 합니다.

        return convertToDto(boardMatching);
    }


}
