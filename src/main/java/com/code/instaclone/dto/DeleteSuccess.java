package com.code.instaclone.dto;

import lombok.Getter;

@Getter
public class DeleteSuccess extends Success {

    public DeleteSuccess(String message) {
        super(message);
    }
}
