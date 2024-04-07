package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.PaginationDto;
import com.gamza.ItEat.enums.TagName;
import com.gamza.ItEat.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "태그별 게시물 조회(8개씩 전달)")
    @GetMapping("/search")
    public PaginationDto findPostByTags(
            @RequestParam List<TagName> tags,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "8") // 8개씩
            int size) {
        PaginationDto allPostByTags = searchService.searchPostByTags(tags, PageRequest.of(page,size));
        return allPostByTags;
    }

}
