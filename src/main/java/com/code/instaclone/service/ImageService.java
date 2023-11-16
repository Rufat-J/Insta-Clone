package com.code.instaclone.service;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.DownloadImageData;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ByteConversionException;
import com.code.instaclone.exception.FailedImageUploadException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public UploadSuccess uploadImage(MultipartFile file, int userId)
            throws ImageSizeTooLargeException, ByteConversionException, FailedImageUploadException {
        try {
            if (isValidImageSize(file)) {
                Image image;
                try {
                    ProfilePage profilePage = findProfilePage(userId);
                    image = new Image(file, profilePage);
                } catch (Exception e) {
                    throw new ByteConversionException("Failed to convert image to bytes");
                }
                imageRepository.save(image);

                return new UploadSuccess("Uploaded successfully");
            } else {
                throw new ImageSizeTooLargeException("File size exceeds the allowed limit of 2 megabytes");
            }
        } catch (Exception e) {
            throw new FailedImageUploadException("Could not upload image: " + e.getMessage());
        }
    }

    public DownloadImageData downloadImage(int imageId) throws ImageDoesNotExistException {
        Image image = getImageById(imageId);
        boolean isExistingImage = (image != null);

        if (isExistingImage) {
            ByteArrayResource resource = new ByteArrayResource(image.getData());

            return new DownloadImageData(resource, image);
        } else {
            throw new ImageDoesNotExistException("Image with id {" + imageId + "} does not exist");
        }
    }

    public boolean isValidImageSize(MultipartFile file) throws ImageSizeTooLargeException {
        long imageSize = file.getSize();
        long maxSize = 2 * 1024 * 1024; // 2mb

        return imageSize <= maxSize;
    }

    @Transactional
    public DeleteSuccess deleteImage(int userId, int imageId) throws ImageDoesNotExistException {
        ProfilePage profilePage = findProfilePage(userId);
        int profilePageId = profilePage.getProfilePageId();
        boolean hasPermissionToDeleteImage = imageRepository.isImageBelongingToProfilePage(profilePageId, imageId);

        if (hasPermissionToDeleteImage) {
            String imageName = findNameByImageId(imageId);
            imageRepository.deleteById(imageId);

            return new DeleteSuccess("Image with Id {" + imageId + "} and image name {" + imageName + "} has been deleted");
        } else
            throw new ImageDoesNotExistException("Image with id {" + imageId + "} does not exist on user profile page");
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