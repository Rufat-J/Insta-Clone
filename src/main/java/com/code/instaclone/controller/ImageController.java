package com.code.instaclone.controller;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.model.Image;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/{profile}/image")
public class ImageController {

    private ImageService imageService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ImageController(ImageService imageService, JwtTokenProvider jwtTokenProvider) {
        this.imageService = imageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/upload")
    public ResponseEntity<UploadSuccess> uploadImage(@RequestParam("image") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        boolean isValid = jwtTokenProvider.validate(token);
        if (isValid) {
            return ResponseEntity.ok(imageService.validateImageSize(file));
        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<List<Image>> downloadImage() {
        System.out.println(imageService.getAllImages());
        return ResponseEntity.ok(imageService.getAllImages());
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