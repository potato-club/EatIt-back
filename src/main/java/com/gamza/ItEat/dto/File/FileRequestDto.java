package com.gamza.ItEat.dto.File;

import com.gamza.ItEat.entity.FileEntity;

public class FileRequestDto {
    private String fileName;
    private String fileUrl;
    private boolean deleted;

    public FileRequestDto(FileEntity file) {
        this.fileName = file.getFileName();
        this.fileUrl = file.getFileUrl();
        this.deleted = true;
    }

}
