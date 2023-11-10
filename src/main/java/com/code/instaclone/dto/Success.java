package com.code.instaclone.dto;


import lombok.Getter;

@Getter
abstract public class Success{

    private String message;

    public Success(String message) {
        this.message = message;
    }
}
