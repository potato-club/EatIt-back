package com.gamza.ItEat.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.gamza.ItEat.dto.File.FileDto;
import com.gamza.ItEat.dto.File.FileRequestDto;
import com.gamza.ItEat.dto.File.FileResponseDto;
import com.gamza.ItEat.dto.File.ImageSaveRequestDto;
import com.gamza.ItEat.entity.FileEntity;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.NotFoundException;
import com.gamza.ItEat.repository.FileRepository;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3Client s3Client;
    private final FileRepository fileRepository;
    private final PostService postService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public List<FileDto> uploadImages(ImageSaveRequestDto requestDto, Long postId) throws IOException {
        PostEntity post = postService.getPostId(postId);
        List<FileEntity> list = this.existsFiles(requestDto.getImages());

        for (FileEntity file : list) {
            file.setPost(post);
            fileRepository.save(file);
        }

        return list.stream()
                .map(FileDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FileDto> updateImages(Long postId, List<MultipartFile> files, List<FileRequestDto> requestDto) throws IOException {
        PostEntity post = postService.getPostId(postId);
        List<FileEntity> existingFiles = fileRepository.findByPost(post);

        List<FileEntity> newFiles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(files)) {
            newFiles = this.existsFiles(files);
        }

        List<FileEntity> resultList = new ArrayList<>();

        // 기존 파일 삭제 및 유지 처리
        for (int i = 0; i < requestDto.size(); i++) {
            FileRequestDto dto = requestDto.get(i);
            for (FileEntity existingFile : existingFiles) {
                if (existingFile.getFileName().equals(dto.getFileName())) {
                    if (dto.isDeleted()) {
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName, existingFile.getFileName()));
                        fileRepository.delete(existingFile);
                    } else {
                        resultList.add(existingFile);
                    }
                    break;
                }
            }
        }

        // 새 파일 추가 처리
        for (FileEntity file : newFiles) {
            file.setPost(post);
            FileEntity savedFile = fileRepository.save(file);
            resultList.add(savedFile);
        }

        return resultList.stream()
                .map(FileDto::new)
                .collect(Collectors.toList());
    }

    public byte[] downloadImage(String key) throws IOException {
        byte[] content;
        final S3Object s3Object = s3Client.getObject(bucketName, key);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            s3Object.close();
        } catch (final IOException ex) {
            throw new IOException("IO Error Message= " + ex.getMessage());
        }
        return content;
    }

    @Transactional
    public void deleteFile(String storedName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, storedName));
            fileRepository.deleteByFileName(storedName);
        } catch (Exception e) {
            throw new NotFoundException("해당 파일을 찾을 수 없습니다.", ErrorCode.NOT_FOUND_EXCEPTION);
        }
    }
    private List<FileEntity> existsFiles(List<MultipartFile> files) throws IOException {
        List<FileEntity> list = new ArrayList<>();

        for (MultipartFile file : files) {
            String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
                String fileUrl = s3Client.getUrl(bucketName, key).toString();

                FileEntity fileEntity = FileEntity.builder()
                        .fileName(file.getOriginalFilename())
                        .fileUrl(fileUrl)
                        .storedName(key)
                        .build();
                list.add(fileEntity);
            }
        }

        return list;
    }
}
