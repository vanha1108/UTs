package com.example.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public interface FileStorageService {

    String storeFile(MultipartFile file);
    Resource loadFileAsResource(String fileName);
}
