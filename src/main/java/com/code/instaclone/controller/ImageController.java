package com.code.instaclone.controller;

import com.code.instaclone.exception.InvalidLoginException;
import com.code.instaclone.model.Image;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.ImageService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        return ResponseEntity.ok(imageService.validateTokenForImage(token, file));
    }

    @GetMapping("/download")
    public ResponseEntity<List<Image>> downloadImage(){
        System.out.println(imageService.getAllImages());
        return ResponseEntity.ok(imageService.getAllImages());
    }

}