package com.code.instaclone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ImageDoesNotExistException extends RuntimeException{
    public ImageDoesNotExistException(String message){
        super(message);
    }
}
