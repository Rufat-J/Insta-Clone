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

    public ImageSizeTooLargeException uploadImage(MultipartFile file) throws IOException {
        byte[] imageData = file.getBytes();
        long imageSize = file.getSize();
        Image image = new Image();
        image.setData(imageData);
        image.setName(file.getOriginalFilename());
        image.setSize(imageSize);
        imageRepository.save(image);
        return null;
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public UploadSuccess validateTokenForImage(String token, MultipartFile file) throws IOException {
        var isValid = jwtTokenProvider.validToken(token);
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 10240;

        if (isValid & imageSize <= maxSize) {
            uploadImage(file);
            return new UploadSuccess("Upload successful");
        } else if (!isValid) {
            throw new InvalidTokenException("Access denied.");

        } else if (imageSize > maxSize) {
            throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
        }
        return null;
    }
}