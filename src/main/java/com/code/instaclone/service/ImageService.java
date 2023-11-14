package com.code.instaclone.service;

import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ImageSizeTooLargeException;
import com.code.instaclone.exception.InvalidLoginException;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.model.Image;
import com.code.instaclone.repository.ImageRepository;
import com.code.instaclone.security.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    ImageRepository imageRepository;
    JwtTokenProvider jwtTokenProvider;



    public ImageService(ImageRepository imageRepository, JwtTokenProvider jwtTokenProvider) {
        this.imageRepository = imageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void uploadImage(MultipartFile file) {
        byte[] imageData;
        try {
        imageData = file.getBytes(); }
        catch (Exception e) {
            throw new RuntimeException("Faild to convert image to bytes");
        }
        long imageSize = file.getSize();
        Image image = new Image();
        image.setData(imageData);
        image.setName(file.getOriginalFilename());
        image.setSize(imageSize);
        imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public UploadSuccess validateImageSize(MultipartFile file) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        if (imageSize <= maxSize) {
            uploadImage(file);
            return new UploadSuccess("Upload successful");
        } else {
            throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
        }
    }
}