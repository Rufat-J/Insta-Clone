package com.code.instaclone.controller;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.DownloadImageData;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.model.Image;
import com.code.instaclone.model.User;
import com.code.instaclone.repository.UserRepository;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    @Autowired
    public ImageController(ImageService imageService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.imageService = imageService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }


    @PostMapping("/upload")
    public ResponseEntity<UploadSuccess> uploadImage(@PathVariable String profile, @RequestParam("image")MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        boolean isValid = jwtTokenProvider.validate(token);
        Optional<User> userOptional = userRepository.findByUsername(profile);

        int userId = jwtTokenProvider.getTokenId(token);

        if (!isValid) {
            throw new InvalidTokenException("Access denied.");
        } else {
            if(userOptional.isPresent()) {
                return ResponseEntity.ok(imageService.validateImageSize(file, userId));
            } else {
                throw new Error("User not found");
            }
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable int imageId , @RequestHeader("Authorization") String token){
        boolean isValid = jwtTokenProvider.validate(token);
        if (isValid) {
            int userId = jwtTokenProvider.getTokenId(token);
            DownloadImageData result = imageService.downloadImage(imageId, userId);
            Image image = result.getImage();
            Resource resource = result.getResource();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getName())
                    .contentType(MediaType.parseMediaType("image/jpeg"))
                    .contentLength(image.getSize())
                    .body(resource);

        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<DeleteSuccess> deleteImage(@PathVariable int imageId, @RequestHeader("Authorization") String token) {
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