package com.code.instaclone.controller;

import com.code.instaclone.dto.LoginSuccess;
import com.code.instaclone.model.ProfilePage;
import com.code.instaclone.model.User;
import com.code.instaclone.security.JwtTokenProvider;
import com.code.instaclone.service.UserService;
import com.sun.net.httpserver.Authenticator;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private record UserDTO(String username, String password){};
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO){
        return userService.register(userDTO.username(), userDTO.password());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccess> login(@RequestBody UserDTO user) {
        var result = userService.login(user.username(), user.password());
        return ResponseEntity.ok().body(result);
    }


}
