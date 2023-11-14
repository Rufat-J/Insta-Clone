package com.code.instaclone.service;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ImageDoesNotExistException;
import com.code.instaclone.exception.ImageSizeTooLargeException;
import com.code.instaclone.model.Image;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.repository.ImageRepository;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.security.JwtTokenProvider;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    ProfilePageRepository profilePageRepository;
    ImageRepository imageRepository;
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ImageService(ProfilePageRepository profilePageRepository, ImageRepository imageRepository, JwtTokenProvider jwtTokenProvider) {
        this.imageRepository = imageRepository;
        this.profilePageRepository = profilePageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void uploadImage(MultipartFile file, int id) {
        byte[] imageData;
        try {
            imageData = file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert image to bytes");
        }
        long imageSize = file.getSize();
        Image image = new Image();

        image.setData(imageData);
        image.setName(file.getOriginalFilename());
        image.setSize(imageSize);

        imageRepository.save(image);
    }

    public ResponseEntity<Resource> downloadImage(Image image) {
        ByteArrayResource resource = new ByteArrayResource(image.getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getName())
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .contentLength(image.getSize())
                .body(resource);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public UploadSuccess validateImageSize(MultipartFile file, int id) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        if (imageSize <= maxSize) {
            uploadImage(file, id);
            return new UploadSuccess("Upload successful");
        } else {
            throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
        }
    }

    @Transactional
    public DeleteSuccess deleteImage(int userId, int imageId) {
        ProfilePage profilePage = findProfilePage(userId);
        int profilePageId = profilePage.getProfilePageId();

        if (imageRepository.isImageBelongingToProfilePage(profilePageId, imageId)) {
            String imageName = findNameByImageId(imageId);
            imageRepository.deleteById(imageId);

            return new DeleteSuccess("Image with Id {" + imageId + "} and image name {" + imageName + "} has been deleted");
        }
        else throw new ImageDoesNotExistException("Image with id {" + imageId + "} does not exist on user profile page");
    }

    private ProfilePage findProfilePage(int userId) {
        Optional<ProfilePage> profilePage = profilePageRepository.findByUserId(userId);
        return profilePage.orElse(null);
    }

    public String findNameByImageId(int imageId) {
        Optional<String> name = imageRepository.findNameByImageId(imageId);
        return name.orElse(null);
    }


    public Image getImageById(int id) {
        return imageRepository.findById(id).orElse(null);
    }
}