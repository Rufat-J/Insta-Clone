package com.code.instaclone.service;

import com.code.instaclone.model.Image;
import com.code.instaclone.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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
}



