package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.File.FileDto;
import com.gamza.ItEat.dto.File.FileRequestDto;
import com.gamza.ItEat.dto.File.ImageSaveRequestDto;
import com.gamza.ItEat.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/s3")
public class FileController {
        private final FileService fileService;

        @Operation(summary = "Upload Files API")
        @PostMapping("/upload")
        public ResponseEntity<List<FileDto>> uploadFiles(@ModelAttribute ImageSaveRequestDto requestDto, @RequestParam Long postId) {
                try {
                        List<FileDto> response = fileService.uploadImages(requestDto, postId);
                        return ResponseEntity.ok(response);
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
                }
        }
        @Operation(summary = "Update Files API")
        @PutMapping("/update")
        public ResponseEntity<List<FileDto>> updateFiles(@RequestParam Long postId, @RequestParam("files") List<MultipartFile> files, @RequestBody List<FileRequestDto> requestDto) {
                try {
                        List<FileDto> updatedFiles = fileService.updateImages(postId, files, requestDto);
                        return ResponseEntity.ok(updatedFiles);
                } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
        }

        @Operation(summary = "S3 Download API")
        @GetMapping("/download")
        public ResponseEntity<InputStreamResource> s3Download(@RequestParam String key) {
                try {
                        byte[] data = fileService.downloadImage(key);
                        InputStream inputStream = new ByteArrayInputStream(data);
                        InputStreamResource resource = new InputStreamResource(inputStream);
                        return ResponseEntity.ok()
                                .contentLength(data.length)
                                .header("Content-type", "application/octet-stream")
                                .header("Content-Disposition", "attachment; filename=" +
                                        URLEncoder.encode(key, "UTF-8"))
                                .body(resource);
                } catch (IOException ex) {
                        return ResponseEntity.badRequest().contentLength(0).body(null);
                }
        }

        @Operation(summary = "Delete File API")
        @DeleteMapping("/delete/{fileName}")
        public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
                try {
                        fileService.deleteFile(fileName);
                        return ResponseEntity.ok().build();
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
        }
}