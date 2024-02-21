package com.gamza.ItEat.dto.post;

import com.gamza.ItEat.enums.CategoryName;
import lombok.Getter;

@Getter
public class RequestPostDto {
    private String title;
    private String content;
    private CategoryName categoryName;
}
