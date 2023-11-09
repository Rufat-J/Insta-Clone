package com.code.instaclone.controller;

import com.code.instaclone.model.Image;
import com.code.instaclone.service.ImageService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    @GetMapping("/getimage")
    public void getJpg() throws IOException {
        List<Image> images = imageService.getAllImages();

        if (!images.isEmpty()) {
            Image firstImage = images.get(0);

            byte[] imageData = firstImage.getData();
            String base64Image = Base64.encodeBase64String(imageData);

            imageService.convertBase64ToJPG(base64Image, "src/main/java/com/code/instaclone/newfile.jpg");
        }
    }

    @GetMapping("/get-last-image")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getJpgm() throws IOException {

        List<Image> images = imageService.getAllImages();
        Image firstImage = images.get(images.size()-1);

        byte[] imageData = firstImage.getData();
        String base64Image = Base64.encodeBase64String(imageData);

        ByteArrayResource resource = new ByteArrayResource(imageData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageData.length);
        headers.setContentDispositionFormData("attachment", "image.jpg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}


