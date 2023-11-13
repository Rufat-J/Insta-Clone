package com.code.instaclone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
public class ImageSizeTooLargeException extends RuntimeException{
    public ImageSizeTooLargeException(String message){
        super(message);
    }
}
