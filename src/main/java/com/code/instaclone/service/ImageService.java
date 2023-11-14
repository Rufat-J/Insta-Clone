package com.code.instaclone.service;

import com.code.instaclone.dto.DeletedImageDto;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ImageDoesNotExistException;
import com.code.instaclone.exception.ImageSizeTooLargeException;
import com.code.instaclone.model.Image;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.repository.ImageRepository;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public void uploadImage(MultipartFile file) {
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

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public UploadSuccess validateImageSize(MultipartFile file) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        if (imageSize <= maxSize) {
            uploadImage(file);
            return new UploadSuccess("Upload successful");
        } else {
            throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
        }
    }

    @Transactional
    public DeletedImageDto deleteImage(int userId, int imageId) {

        // hitta profile page
        ProfilePage profilePage = findProfilePage(userId);
        int profilePageId = profilePage.getProfilePageId();

        //hitta img på profilepage
        if (imageRepository.isImageBelongingToProfilePage(profilePageId, imageId)) {
            Image image = findImage(imageId);
            imageRepository.deleteById(imageId);

            byte[] imageData = image.getData();
            ByteArrayResource resource = new ByteArrayResource(imageData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageData.length);
            headers.setContentDispositionFormData("attachment", image.getName());

            return new DeletedImageDto(headers, resource);


        } else {
            throw new ImageDoesNotExistException("Image with id {" + imageId + "} does not exist on user profile page");
        }
    }

    private ProfilePage findProfilePage(int userId) {
        Optional<ProfilePage> profilePage = profilePageRepository.findByUserId(userId);
        return profilePage.orElse(null);
    }

    @Transactional
    private Image findImage(int imageId) {
        Optional<Image> image = imageRepository.findByImageId(imageId);
        return image.orElse(null);
    }
}