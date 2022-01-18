package com.example.demo.repository.custom;

import com.example.demo.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardDto.getQuery> getBoardList(Pageable pageable);
}
