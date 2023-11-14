package com.code.instaclone.service;

import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ImageSizeTooLargeException;
import com.code.instaclone.exception.InvalidLoginException;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.model.Image;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.repository.ImageRepository;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.security.JwtTokenProvider;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    ImageRepository imageRepository;
    JwtTokenProvider jwtTokenProvider;

    ProfilePageRepository profilePageRepository;


    public ImageService(ImageRepository imageRepository, JwtTokenProvider jwtTokenProvider, ProfilePageRepository profilePageRepository) {
        this.imageRepository = imageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.profilePageRepository = profilePageRepository;
    }

    public void uploadImage(MultipartFile file, int id) {
        byte[] imageData;
        try {
        imageData = file.getBytes(); }
        catch (Exception e) {
            throw new RuntimeException("Failed to convert image to bytes");
        }
        long imageSize = file.getSize();
        Image image = new Image();

        image.setData(imageData);
        image.setName(file.getOriginalFilename());
        image.setSize(imageSize);

        imageRepository.save(image);
    }

    public ResponseEntity<Resource> downloadImage(Image image) {
        ByteArrayResource resource = new ByteArrayResource(image.getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getName())
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .contentLength(image.getSize())
                .body(resource);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public UploadSuccess validateImageSize(MultipartFile file, int id) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        if (imageSize <= maxSize) {
            uploadImage(file, id);
            return new UploadSuccess("Upload successful");
        } else {
            throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
        }
    }

    public Image getImageById(int id) {
        return imageRepository.findById(id).orElse(null);
    }
}