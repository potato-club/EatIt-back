package com.gamza.ItEat.dto.File;

import com.gamza.ItEat.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class FileDto {
    private String fileName;

    private String fileUrl;

    public FileDto(FileEntity file) {
        this.fileName = file.getFileName();
        this.fileUrl = file.getFileUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDto)) return false;
        FileDto dto = (FileDto) o;
        return Objects.equals(this.fileName, dto.getFileName()) &&
                Objects.equals(this.fileUrl, dto.getFileUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fileName, this.fileUrl);
    }
}
