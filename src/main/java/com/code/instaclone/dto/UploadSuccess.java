package com.code.instaclone.dto;


import lombok.Getter;

@Getter
public class UploadSuccess extends Success{

    public UploadSuccess(String message) {
        super(message);
    }

}
