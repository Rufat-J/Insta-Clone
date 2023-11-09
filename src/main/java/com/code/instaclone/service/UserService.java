package com.code.instaclone.service;

import com.code.instaclone.model.User;
import com.code.instaclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> register(String username, String password) {
        User newUser = new User(username, password);

        try {
            userRepository.save(newUser);
            return ResponseEntity.ok(newUser);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }

    }

    public ResponseEntity<Object> login(String username, String password) {
        if(userRepository.isValidUser(username, password)) {
            System.out.println("Logged in");
        } else System.out.println("invalid");

        return null;
    }

}
