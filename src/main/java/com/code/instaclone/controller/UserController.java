package com.code.instaclone.controller;

import com.code.instaclone.dto.LoginSuccess;
import com.code.instaclone.dto.RegisterSuccess;
import com.code.instaclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private record UserDTO(String username, String password){};

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterSuccess> registerUser(@RequestBody UserDTO userDTO){
        var result = userService.register(userDTO.username(), userDTO.password());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccess> login(@RequestBody UserDTO user) {
        var result = userService.login(user.username(), user.password());
        return ResponseEntity.ok().body(result);
    }

}
