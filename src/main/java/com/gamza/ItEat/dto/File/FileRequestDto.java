package com.gamza.ItEat.dto.File;

import com.gamza.ItEat.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class FileRequestDto {
    private String fileName;
    private boolean isDeleted;
}
