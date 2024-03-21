package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.PaginationDto;
import com.gamza.ItEat.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

//    @Operation(summary = "태그 기반으로 게시물 검색")
//    @GetMapping("/search")
//    public PaginationDto searchPostByTags(@RequestParam(value = "tags") List<String> tagNames, Pageable pageable) {
//        PaginationDto result = searchService.searchPostByTags(tagNames, pageable);
//    }

}
