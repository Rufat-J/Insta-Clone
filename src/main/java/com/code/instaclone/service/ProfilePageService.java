package com.code.instaclone.service;

import com.code.instaclone.dto.EditSuccess;
import com.code.instaclone.exception.ProfilePageNotFoundException;
import com.code.instaclone.exception.UnauthorizedEditException;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.model.User;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilePageService {

    ProfilePageRepository profilePageRepository;
    UserRepository userRepository;

    @Autowired
    public ProfilePageService(ProfilePageRepository profilePageRepository, UserRepository userRepository) {
        this.profilePageRepository = profilePageRepository;
        this.userRepository = userRepository;
    }

    public EditSuccess editProfileDescription(int userId, String newDescription) {
        boolean hasPermissionToEdit = profilePageRepository.isProfilePageBelongingToUser(userId);

        if (hasPermissionToEdit) {
            ProfilePage profilePage = findByUserId(userId);
            profilePage.setDescription(newDescription);
            profilePageRepository.save(profilePage);

            return new EditSuccess("Description was successfully updated");

        } else {
            throw new UnauthorizedEditException("User with id: {" + userId + "} does not have permission to edit this profile page");
        }
    }

    private ProfilePage findByUserId(int userId) {
        Optional<ProfilePage> profilePage = profilePageRepository.findByUserId(userId);
        return profilePage.orElse(null);
    }

    @Transactional
    public ProfilePage getProfilePage(String username) {
        try {
            User user = findUserByUsername(username);
            int userId = user.getId();
            return findProfilePageByUserId(userId);
        } catch (Exception e) {
            throw new ProfilePageNotFoundException("Profile page with username: {" + username + "} could not be found");
        }
    }

    private User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    private ProfilePage findProfilePageByUserId(int userId) {
        Optional<ProfilePage> profilePage = profilePageRepository.getUserProfileInfo(userId);
        return profilePage.orElse(null);
    }
}