package com.code.instaclone.controller;

import com.code.instaclone.dto.EditSuccess;
import com.code.instaclone.dto.NewDescriptionDto;
import com.code.instaclone.exception.InvalidTokenException;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.ProfilePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/profilepage")
public class ProfilePageController {

    private ProfilePageService profilePageService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ProfilePageController(ProfilePageService profilePageService, JwtTokenProvider jwtTokenProvider) {
        this.profilePageService = profilePageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PutMapping("/edit/description")
    public ResponseEntity<EditSuccess> downloadImage(@RequestBody NewDescriptionDto dto, @RequestHeader("Authorization") String token) {
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
    public ResponseEntity<Map<String, Object>> getProfilePageJson(@PathVariable String username, @RequestHeader("Authorization") String token) {
        boolean isValid = jwtTokenProvider.validate(token);

        if (isValid) {
            ProfilePage profilePage = profilePageService.getProfilePage(username);
            return ResponseEntity.ok(profilePage.toJson());

        } else {
            throw new InvalidTokenException("Access denied.");
        }
    }
}