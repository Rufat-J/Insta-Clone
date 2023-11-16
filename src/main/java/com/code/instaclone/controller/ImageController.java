package com.code.instaclone.controller;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.DownloadImageData;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ImageDoesNotExistException;
import com.code.instaclone.exception.ImageSizeTooLargeException;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ImageController(ImageService imageService, JwtTokenProvider jwtTokenProvider) {
        this.imageService = imageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadSuccess> uploadImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("image") MultipartFile file)
            throws InvalidTokenException,
            ImageSizeTooLargeException {
        boolean isValid = jwtTokenProvider.validate(token);

        if (isValid) {
            int userId = jwtTokenProvider.getTokenId(token);
            return ResponseEntity.ok(imageService.uploadImage(file, userId));
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int imageId, @RequestHeader("Authorization") String token)
            throws InvalidTokenException, ImageDoesNotExistException {
        boolean isValid = jwtTokenProvider.validate(token);

        if (isValid) {
            DownloadImageData result = imageService.downloadImage(imageId);

            return result.toResponseEntity();
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<DeleteSuccess> deleteImage(@PathVariable int imageId, @RequestHeader("Authorization") String token)
            throws InvalidTokenException, ImageDoesNotExistException {
        boolean isValid = jwtTokenProvider.validate(token);
        if (isValid) {
            int userId = jwtTokenProvider.getTokenId(token);
            DeleteSuccess result = imageService.deleteImage(userId, imageId);

            return ResponseEntity.ok(result);
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}