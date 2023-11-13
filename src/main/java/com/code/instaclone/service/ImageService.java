package com.code.instaclone.service;

import com.code.instaclone.dto.LoginSuccess;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.InvalidLoginException;
import com.code.instaclone.model.Image;
import com.code.instaclone.repository.ImageRepository;
import com.code.instaclone.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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

    public void uploadImage(MultipartFile file) throws IOException {
        byte[] imageData = file.getBytes();

        Image image = new Image();
        image.setData(imageData);
        image.setName(file.getOriginalFilename());

        imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public UploadSuccess validateTokenForImage(String token, MultipartFile file) throws IOException {
        var isValid = jwtTokenProvider.validToken(token);
        if(isValid) {
            uploadImage(file);
            return new UploadSuccess("Upload successful");
        }
        else throw new InvalidLoginException("Token validation failed");
    }
}