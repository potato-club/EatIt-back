package com.gamza.ItEat.dto.File;

import com.gamza.ItEat.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class FileDto {
    private String fileName;
    private String fileUrl;

    public FileDto(FileEntity fileEntity) {
        this.fileName = fileEntity.getFileName();
        this.fileUrl = fileEntity.getFileUrl();
    }
}
