package com.gamza.ItEat.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gamza.ItEat.enums.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePostDto {

    private Long id;
    private String title;
    private String content;
    private CategoryName categoryName;
    private int views;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

}
