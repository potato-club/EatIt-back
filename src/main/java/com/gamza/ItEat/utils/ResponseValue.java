package com.gamza.ItEat.utils;

import com.gamza.ItEat.dto.post.PaginationDto;
import com.gamza.ItEat.dto.post.ResponsePostDto;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.BadRequestException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ResponseValue {


    public static ResponsePostDto getAllBuild(PostEntity postEntity) {
        if (postEntity != null) {
            return ResponsePostDto.builder()
                    .id(postEntity.getId())
                    .title(postEntity.getTitle())
                    .content(postEntity.getContent())
                    .views(postEntity.getViews())
                    .categoryName(postEntity.getCategory().getCategoryName())
                    .mentor(postEntity.isMentor())
                    .commentNums(postEntity.getCommentsNum())
                    .createdAt(postEntity.getCreatedAt())
                    .updatedAt(postEntity.getModifiedAt())
                    .startDate(postEntity.getStartDate())
                    .endDate(postEntity.getEndDate())
                    .build();
        } else {
            throw new BadRequestException("존재하는 게시물이 없습니다!", ErrorCode.RUNTIME_EXCEPTION);
        }
    }

    public static ResponsePostDto getOneBuild(PostEntity postEntity) {
        if(postEntity != null) {
            return ResponsePostDto.builder()
                    .id(postEntity.getId())
                    .title(postEntity.getTitle())
                    .content(postEntity.getContent())
                    .views(postEntity.getViews())
                    .commentNums(postEntity.getCommentsNum())
                    .mentor(postEntity.isMentor())
                    .categoryName(postEntity.getCategory().getCategoryName())
                    .createdAt(postEntity.getCreatedAt())
                    .startDate(postEntity.getStartDate())
                    .endDate(postEntity.getEndDate())
                    .build();
        } else {
            throw new BadRequestException("존재하는 게시물이 없습니다!", ErrorCode.RUNTIME_EXCEPTION);
        }
    }

    public static PaginationDto getPaginationDto(Long pageSize, boolean isLast, Long size, List categoryList) {
        return PaginationDto.builder()
                .totalPage(pageSize)
                .lastPage(isLast)
                .totalElement(size)
                .categoryList(categoryList)
                .build();
    }

}

