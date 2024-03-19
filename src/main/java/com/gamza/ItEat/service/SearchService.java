package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.post.PaginationDto;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.repository.PostRepository;
import com.gamza.ItEat.utils.ResponseValue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;

//    public PaginationDto searchPostByTags(List<String> tagNames, Pageable pageable) {
//        List<Page<PostEntity>> pages = tagNames.stream()
//                .map(tagName -> postRepository.findByTagNames(tagName, pageable))
//                .collect(Collectors.toList());
//
//        return pages.stream()
//                .reduce(ResponseValue::getPaginationDto)
//                .orElse(Page.empty());
//    }
}
