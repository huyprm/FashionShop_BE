package org.ptithcm2021.fashionshop.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class FileController {
    private final FileService fileService;

    @GetMapping("/{fileName}")
    public ApiResponse<InputStreamResource> getAvatar(@PathVariable String fileName, @RequestParam String imageType) throws FileNotFoundException {
       return ApiResponse.<InputStreamResource>builder().data(fileService.loadFile(imageType, fileName)).build();
    }

    @PostMapping("/store")
    public ApiResponse<List<String>> storeFile(@RequestParam("files") List<MultipartFile> files, @RequestParam String imageType){
        List<String> paths = files.stream().map(multipartFile -> {
            try {
                return fileService.storeFile(multipartFile, imageType);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return ApiResponse.<List<String>>builder().data(paths).build();
    }
}
