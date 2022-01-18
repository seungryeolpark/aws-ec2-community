package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final BoardService boardService;

    @RequestMapping(method = RequestMethod.GET)
    public String main(
            Model model,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BoardDto.getQuery> page = boardService.getBoardList(pageable);

        List<BoardDto.getQuery> boardDTOList = page.getContent();
        BoardDto.page boardPageDto = BoardDto.page.builder()
                .total(page.getTotalPages())
                .page(page.getNumber())
                .build();

        model.addAttribute("boardDTOList", boardDTOList);
        model.addAttribute("boardPageDto", boardPageDto);
        return "board/board";
    }
}
