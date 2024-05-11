package com.gamza.ItEat.dto.post;

import com.gamza.ItEat.enums.CategoryName;
import com.gamza.ItEat.enums.TagName;
import lombok.Getter;

import java.util.List;

@Getter
public class RequestPostDto {
    private String title;
    private String content;
    private CategoryName categoryName;
    private List<TagName> tags;
    private boolean mentor;
}
