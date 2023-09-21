package com.bloom.pium.data.repository;


import com.bloom.pium.data.entity.BoardMatching;
import com.bloom.pium.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardMatching,Long> {


    // 게시물을 페이징하여 가져옴
    Page<BoardMatching> findAll(Pageable pageable);

    List<BoardMatching> findByCategory(Category category);


}
