package com.bloom.pium.service;

import com.bloom.pium.data.dto.BoardResponseDto;
import com.bloom.pium.data.dto.CategoryResponseDto;
import com.bloom.pium.data.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    // 카테고리 생성
    CategoryResponseDto createCategory(CategoryDto categoryDto);
    // 전체 카테고리
    List<CategoryResponseDto> getAllCategories();
    // 특정 카테고리
    CategoryResponseDto getCategoryById(Long id);
    // 카테고리 수정
    CategoryResponseDto updateCategory(Long id, CategoryDto categoryDto);
    // 카테고리 삭제
    void deleteCategory(Long id);
    // 특정 카테고리의 게시글 불러오기
    List<BoardResponseDto> getBoardMatchingByCategory(Long categoryId);

}
