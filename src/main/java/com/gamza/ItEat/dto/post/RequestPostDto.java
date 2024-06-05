package com.gamza.ItEat.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gamza.ItEat.enums.CategoryName;
import com.gamza.ItEat.enums.TagName;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RequestPostDto {
    private String title;
    private String content;
    private CategoryName categoryName;
    private List<TagName> tags;
    private boolean mentor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate startDate; // 모집 시작 날짜

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate endDate;   // 모집 종료 날짜
}
