package com.gamza.ItEat.dto.post;

import com.gamza.ItEat.enums.TagName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagInfoDto {
    private Long id;
    private TagName tag;
}
