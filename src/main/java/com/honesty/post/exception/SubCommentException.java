package com.honesty.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SubCommentException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SubCommentNotFound extends RuntimeException{
        public SubCommentNotFound(String message) {
            super(message);
        }
    }
}
