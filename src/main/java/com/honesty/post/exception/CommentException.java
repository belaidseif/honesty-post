package com.honesty.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CommentException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CommentNotFound extends RuntimeException{
        public CommentNotFound(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public static class NotSameUser extends RuntimeException{
        public NotSameUser(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public static class SameAction extends RuntimeException{
        public SameAction(String message) {
            super(message);
        }
    }
}
