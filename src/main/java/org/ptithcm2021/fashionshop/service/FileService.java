package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.FilteredObjectInputStream;
import org.ptithcm2021.fashionshop.configure.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
@Service
public class FileService {
    private final Path location;

    @Autowired
    public FileService(StorageProperties storageProperties) {
        if(storageProperties.getLocation() == null){
            throw new IllegalArgumentException("Location is null.");
        }

        this.location = Paths.get(storageProperties.getLocation());
    }

    public void init(String imageType){
        try{
            Path path = Paths.get(location.toAbsolutePath().toString(), imageType);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            log.info(e.toString());
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String storeFile(MultipartFile file, String imageType) throws UnsupportedEncodingException {
        if(file.isEmpty()) throw new IllegalArgumentException("File is empty.");

        String fileName = URLEncoder.encode(System.currentTimeMillis() + "-" + file.getOriginalFilename(), StandardCharsets.UTF_8.toString());

        Path destinationFile = Paths.get(location.toString(), imageType, fileName);

        init(imageType);

        try(InputStream input = file.getInputStream()){
            Files.copy(input, destinationFile, REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageType + "/"+ fileName;
    }

    public InputStreamResource loadFile(String fileName, String imageType) throws FileNotFoundException {
        Path path = Paths.get(location.toString(), imageType, fileName); // Đảm bảo location là chuỗi hợp lệ
        File file = path.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        return new InputStreamResource(new FileInputStream(file));
    }

    public void deleteFile(String imageType, String fileName) {
        Path path = Paths.get(String.valueOf(location), imageType, fileName);
        File file = path.toFile();
        if(file.exists()) file.delete();
    }

    public void deleteFiles(String imageType, List<String> fileNames) {
        for (String imagePath : fileNames) {
            File file = new File(imagePath);
            if (file.exists() && file.delete()) {
                System.out.println("Đã xóa ảnh: " + imagePath);
            } else {
                System.out.println("Không thể xóa ảnh: " + imagePath);
            }
        }
    }

}


