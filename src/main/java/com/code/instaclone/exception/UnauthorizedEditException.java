package com.code.instaclone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedEditException extends RuntimeException {
    public UnauthorizedEditException(String message) {
        super(message);
    }
}
