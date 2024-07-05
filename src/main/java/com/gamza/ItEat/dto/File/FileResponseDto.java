package com.gamza.ItEat.dto.File;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {
    private String originalName;
    private String storedName;
    private String accessUrl;
}