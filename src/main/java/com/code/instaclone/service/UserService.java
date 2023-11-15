package com.code.instaclone.service;

import com.code.instaclone.dto.LoginSuccess;
import com.code.instaclone.exception.InvalidLoginException;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.model.User;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.repository.UserRepository;
import com.code.instaclone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final ProfilePageRepository profilePageRepository;



    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, ProfilePageRepository profilePageRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.profilePageRepository = profilePageRepository;
    }

    public ResponseEntity<Object> register(String username, String password) {
        User newUser = new User(username, password);
        try {
            userRepository.save(newUser);
           ProfilePage newProfilePage = setupProfilePage(newUser);
            return ResponseEntity.ok(newProfilePage.toJson());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public ProfilePage setupProfilePage(User user) {
        ProfilePage profilePage = new ProfilePage(user);
        profilePageRepository.save(profilePage);
        return profilePage;
    }

    public LoginSuccess login(String username, String password) {
        if (userRepository.isValidUser(username, password)) {
            User user = findUserByUsername(username);
            String token = jwtTokenProvider.generateToken(user);

            return new LoginSuccess("Login successful", token);

        } else throw new InvalidLoginException("Login failed, incorrect username or password");

    }


    private User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public ProfilePage searchUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        int userId = user.get().getId();
        Optional<ProfilePage> profilePage = profilePageRepository.getUserProfileInfo(userId);
        //System.out.println("username: " + username + " userId " + userId);
        return profilePage.orElse(null);
    }
}
