package com.bloom.pium.service.impl;

import com.bloom.pium.data.dto.BoardResponseDto;
import com.bloom.pium.data.dto.CategoryResponseDto;
import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.Category;
import com.bloom.pium.data.entity.UserInfo;
import com.bloom.pium.data.repository.BoardRepository;
import com.bloom.pium.data.repository.CategoryRepository;
import com.bloom.pium.data.dto.CategoryDto;
import com.bloom.pium.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    // ↓↓ 추가 (2023.09.17.일)

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               BoardRepository boardRepository) {
        this.categoryRepository = categoryRepository;
        this.boardRepository = boardRepository;
    }

    private CategoryResponseDto convertToResponseDto(Category category) {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(category.getId());
        responseDto.setName(category.getName());
        return responseDto;
    }

    @Override // 카테고리 생성
    public CategoryResponseDto createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return convertToResponseDto(savedCategory);
    }

    @Override // 전체 카테고리
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override // 특정 카테고리
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToResponseDto(category);
    }

    @Override // 카테고리 수정
    public CategoryResponseDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return convertToResponseDto(updatedCategory);
    }

    @Override // 카테고리 삭제
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    private BoardResponseDto convertToDto(BoardMatching boardMatching) {
        BoardResponseDto boardResponseDto = new BoardResponseDto();
        boardResponseDto.setBoardId(boardMatching.getBoardId());
        boardResponseDto.setTitle(boardMatching.getTitle());
        boardResponseDto.setContent(boardMatching.getContent());
        boardResponseDto.setViewCnt(boardMatching.getViewCnt());
        boardResponseDto.setLikeCnt(boardMatching.getLikeCnt());
        boardResponseDto.setCreatedDate(boardMatching.getCreatedDate());
        boardResponseDto.setModifiedDate(boardMatching.getModifiedDate());
        boardResponseDto.setCommentCount(boardMatching.getCommentCount());

        // Fetch the category name from the board's category
        String categoryName = boardMatching.getCategory() != null ? boardMatching.getCategory().getName() : null;
        boardResponseDto.setCategoryName(categoryName);

        // Fetch the username from the associated UserInfo
        UserInfo userInfo = boardMatching.getUserInfo();
        if (userInfo != null) {
            boardResponseDto.setUsername(userInfo.getUsername());
        }
        return boardResponseDto;
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
    // ↑↑ 추가 (2023.09.17.일)
}