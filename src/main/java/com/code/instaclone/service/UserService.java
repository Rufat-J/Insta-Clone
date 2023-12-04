package com.code.instaclone.service;

import com.code.instaclone.dto.LoginSuccess;
import com.code.instaclone.dto.RegisterSuccess;
import com.code.instaclone.exception.InvalidLoginException;
import com.code.instaclone.exception.RegistrationFailureException;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.model.User;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.repository.UserRepository;
import com.code.instaclone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RegisterSuccess register(String username, String password) {
        User newUser = new User(username, password);
        try {
            userRepository.save(newUser);
            setupProfilePage(newUser);
            return new RegisterSuccess("Account with username: {" + username + "} Registered successfully");

        } catch (Exception e) {
            throw new RegistrationFailureException("Registration failed: " + e.getMessage());
        }
    }

    public void setupProfilePage(User user) {
        ProfilePage profilePage = new ProfilePage(user);
        profilePageRepository.save(profilePage);
    }

    public LoginSuccess login(String username, String password) {
        boolean isValidUserDetails = userRepository.isValidUser(username, password);

        if (isValidUserDetails) {
            User user = findUserByUsername(username);
            String token = jwtTokenProvider.generateToken(user);

            return new LoginSuccess("Login successful", token);

        } else {
            throw new InvalidLoginException("Login failed, incorrect username or password");
        }
    }

    private User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }
}
