package com.honesty.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PostException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class PostNotFound extends RuntimeException{
        public PostNotFound(String message) {
            super(message);
        }
    }
}
