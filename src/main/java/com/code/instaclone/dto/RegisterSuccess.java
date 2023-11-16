package com.code.instaclone.dto;

import lombok.Getter;

@Getter
public class RegisterSuccess extends Success {

    public RegisterSuccess(String message) {
        super(message);
    }

}
