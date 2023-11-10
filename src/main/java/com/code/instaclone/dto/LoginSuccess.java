package com.code.instaclone.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginSuccess extends Success{

    private String token;

    public LoginSuccess(String message, String token) {
        super(message);
        this.token = token;
    }

}
