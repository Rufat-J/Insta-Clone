package com.code.instaclone.controller;

import com.code.instaclone.dto.DeleteSuccess;
import com.code.instaclone.dto.DownloadImageData;
import com.code.instaclone.dto.EditSuccess;
import com.code.instaclone.dto.UploadSuccess;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.model.Image;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.model.User;
import com.code.instaclone.repository.ProfilePageRepository;
import com.code.instaclone.repository.UserRepository;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.ImageService;
import com.code.instaclone.service.ProfilePageService;
import com.code.instaclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profilepage")
public class ProfilePageController {

    private ProfilePageService profilePageService;
    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @Autowired
    public ProfilePageController(ProfilePageService profilePageService, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.profilePageService = profilePageService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    record NewDescriptionDto(String description) {
    }

    @PutMapping("/edit/description")
    public ResponseEntity<EditSuccess> downloadImage(@RequestHeader("Authorization") String token, @RequestBody NewDescriptionDto dto) {
        boolean isValid = jwtTokenProvider.validate(token);

        if (isValid) {
            int userId = jwtTokenProvider.getTokenId(token);
            EditSuccess result = profilePageService.editProfileDescription(userId, dto.description());

            return ResponseEntity.ok(result);

        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> searchProfile(@PathVariable String username, @RequestHeader("Authorization") String token) {
        boolean isValid = jwtTokenProvider.validate(token);

        if (isValid) {
            ProfilePage profilePage = profilePageService.getProfilePage(username);
            return ResponseEntity.ok(profilePage.toJson());

        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }



}