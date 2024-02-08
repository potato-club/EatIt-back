package com.gamza.ItEat.dto.post;

import lombok.Data;

@Data
public class RequestUpdatePostDto {

    private String title;
    private String content;

    private RequestUpdatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
