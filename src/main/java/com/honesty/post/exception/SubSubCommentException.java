package com.honesty.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SubSubCommentException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SubSubCommentNotFound extends RuntimeException{
        public SubSubCommentNotFound(String message) {
            super(message);
        }
    }
}
