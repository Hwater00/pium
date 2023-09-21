package com.bloom.pium.controller;

import com.bloom.pium.data.dto.BoardResponseDto;
import com.bloom.pium.data.dto.CategoryResponseDto;
import com.bloom.pium.data.dto.CategoryDto;
import com.bloom.pium.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    // ↓↓ 추가 (2023.09.17.일)
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ApiOperation(value = "카테고리 생성")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "전체 카테고리")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    @ApiOperation(value = "특정 카테고리") // 크게 의미 없는 기능
//    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
//        CategoryResponseDto category = categoryService.getCategoryById(id);
//        return new ResponseEntity<>(category, HttpStatus.OK);
//    }

    @PutMapping("/{id}")
    @ApiOperation(value = "카테고리 수정")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "카테고리 삭제")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping("/{id}/board-matching")
//    @ApiOperation(value = "특정 카테고리의 게시글 불러오기") // 주기능
//    public List<BoardResponseDto> getBoardMatchingByCategory(@PathVariable Long id) {
//        return categoryService.getBoardMatchingByCategory(id);
//    }
    // ↑↑ 추가 (2023.09.17.일)

    // ↓↓ 이거 작동 됨
    @GetMapping("/{id}/list")
    @ApiOperation(value = "카테고리 ▶ 게시글 리스트")
    public ModelAndView getBoardListByCategory(@PathVariable Long id, Model model) {
        List<BoardResponseDto> boards = categoryService.getBoardMatchingByCategory(id);
        ModelAndView modelAndView = new ModelAndView("mainPage"); // Thymeleaf 템플릿의 경로
        modelAndView.addObject("boards", boards);
        System.out.println(boards);
        return modelAndView;
    }
    // ↑↑ 이거 작동 됨



}
