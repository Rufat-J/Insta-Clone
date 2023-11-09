package com.code.instaclone.controller;

import com.code.instaclone.model.Image;
import com.code.instaclone.service.ImageService;
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

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body("Upload");
    }

    @GetMapping("/download")
    public ResponseEntity<List<Image>> downloadImage(){
        System.out.println(imageService.getAllImages());
        return ResponseEntity.ok(imageService.getAllImages());
    }

}