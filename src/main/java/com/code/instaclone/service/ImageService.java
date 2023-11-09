package com.code.instaclone.service;

import com.code.instaclone.model.Image;
import com.code.instaclone.repository.ImageRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

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

    public void convertBase64ToJPG(String base64Image, String outputFilePath) throws IOException {
        // Decode Base64 string to bytes
        byte[] imageBytes = Base64.decodeBase64(base64Image);

        // Convert bytes to BufferedImage
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // Save BufferedImage as JPG file
        File outputFile = new File(outputFilePath);
        ImageIO.write(bufferedImage, "jpg", outputFile);
    }


}