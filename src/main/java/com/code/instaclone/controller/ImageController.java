package com.code.instaclone.controller;

import com.code.instaclone.model.Image;
import com.code.instaclone.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{profile}/image")
public class ImageController {

private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /*
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file")) {

    }

     */
}
