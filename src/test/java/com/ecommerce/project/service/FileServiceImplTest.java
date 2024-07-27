package com.ecommerce.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileServiceImpl fileService;

    private String path;

    @BeforeEach
    void setUp() {
        path = "uploads";
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testUploadImage() throws IOException {
//        String originalFileName = "test.jpg";
//        String fixedUUID = "00000000-0000-0000-0000-000000000000";
//        String newFileName = fixedUUID + ".jpg";
//        String filePath = path + File.separator + newFileName;
//
//        File folder = new File(path);
//        if (!folder.exists()) {
//            folder.mkdir();
//        }
//
//        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
//        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
//        when(multipartFile.getInputStream()).thenReturn(inputStream);
//
//        try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
//            mockedUUID.when(UUID::randomUUID).thenReturn(UUID.fromString(fixedUUID));
//            String result = fileService.uploadImage(path, multipartFile);
//            assertEquals(newFileName, result);
//        }
//
//        verify(multipartFile, times(1)).getOriginalFilename();
//        verify(multipartFile, times(1)).getInputStream();
//        assertEquals(true, Files.exists(Paths.get(filePath)));
//
//        // Clean up the file after the test
//        Files.deleteIfExists(Paths.get(filePath));
//    }
//
//    @Test
//    void testUploadImage_CreatesDirectoryIfNotExists() throws IOException {
//        String originalFileName = "test.jpg";
//        String fixedUUID = "00000000-0000-0000-0000-000000000000";
//        String newFileName = fixedUUID + ".jpg";
//        String filePath = path + File.separator + newFileName;
//
//        File folder = new File(path);
//        if (folder.exists()) {
//            folder.delete();
//        }
//
//        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
//        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
//        when(multipartFile.getInputStream()).thenReturn(inputStream);
//
//        try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
//            mockedUUID.when(UUID::randomUUID).thenReturn(UUID.fromString(fixedUUID));
//            String result = fileService.uploadImage(path, multipartFile);
//            assertEquals(newFileName, result);
//        }
//
//        verify(multipartFile, times(1)).getOriginalFilename();
//        verify(multipartFile, times(1)).getInputStream();
//        assertEquals(true, Files.exists(Paths.get(filePath)));
//        assertEquals(true, folder.exists());
//
//        // Clean up the file and directory after the test
//        Files.deleteIfExists(Paths.get(filePath));
//        folder.delete();
//    }

    @Test
    void testUploadImage_ThrowsIOException() throws IOException {
        String originalFileName = "test.jpg";

        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getInputStream()).thenThrow(new IOException("Test IOException"));

        IOException exception = assertThrows(IOException.class, () -> {
            fileService.uploadImage(path, multipartFile);
        });

        assertEquals("Test IOException", exception.getMessage());
        verify(multipartFile, times(1)).getOriginalFilename();
        verify(multipartFile, times(1)).getInputStream();
    }
}
