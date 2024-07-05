package com.gamza.ItEat.dto.File;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageSaveRequestDto {
    private List<MultipartFile> images;
}