package com.code.instaclone.service;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.DownloadImageData;
import com.code.instaclone.dto.EditSuccess;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.ImageDoesNotExistException;
import com.code.instaclone.exception.ImageSizeTooLargeException;
import com.code.instaclone.exception.UnauthorizedEditException;
import com.code.instaclone.model.Image;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.model.User;
import com.code.instaclone.repository.ImageRepository;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.repository.UserRepository;
import com.code.instaclone.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilePageService {

    ProfilePageRepository profilePageRepository;

    public ProfilePageService(ProfilePageRepository profilePageRepository) {
        this.profilePageRepository = profilePageRepository;
    }

    public EditSuccess editProfileDescription(int userId, String newDescription) {
        if (profilePageRepository.isProfilePageBelongingToUser(userId)) {
            ProfilePage profilePage = findByUserId(userId);
            profilePage.setDescription(newDescription);
            profilePageRepository.save(profilePage);

            return new EditSuccess("Description was successfully updated");

        } else {
            throw new UnauthorizedEditException("Profile page belonging to user with id {" + userId + "} either does not exist or you do not have permission to edit it");
        }

    }

    private ProfilePage findByUserId(int userId) {
        Optional<ProfilePage> profilePage = profilePageRepository.findByUserId(userId);
        return profilePage.orElse(null);
    }

    /*

    public Object searchProfile(String username, int userId) {
        int userId = profilePageRepository.findUserIdByUsername(username);
        Optional<ProfilePage> profilePage = UserRepository.findByUsername(username);
        return profilePage.orElse(null);
    }

     */

}