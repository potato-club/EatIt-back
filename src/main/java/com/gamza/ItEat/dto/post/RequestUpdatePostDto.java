package com.gamza.ItEat.dto.post;

import com.gamza.ItEat.enums.CategoryName;
import lombok.Data;

@Data
public class RequestUpdatePostDto {

    private String title;
    private String content;
    private CategoryName category;

    private RequestUpdatePostDto(String title, String content, CategoryName category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}
