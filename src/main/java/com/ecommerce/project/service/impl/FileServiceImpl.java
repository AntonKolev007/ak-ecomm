package com.ecommerce.project.service.impl;

import com.ecommerce.project.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        // current filename
        String originalFileName = image.getOriginalFilename();
        //Generate unique filename
        String randomId = UUID.randomUUID().toString();
        String newFileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + newFileName;
        // Directory exists/ path exists -> if not create;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        //upload to location/server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        //return filename
        return newFileName;
    }
}
