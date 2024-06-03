package com.gamza.ItEat.dto.post;

import com.gamza.ItEat.enums.CategoryName;
import com.gamza.ItEat.enums.TagName;
import lombok.Data;

import java.util.List;

@Data
public class RequestUpdatePostDto {

    private String title;
    private String content;
    private CategoryName category;
    private List<TagName> tags;

    private RequestUpdatePostDto(String title, String content, CategoryName category, List<TagName> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
    }

}
