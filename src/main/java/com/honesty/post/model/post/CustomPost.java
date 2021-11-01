package com.honesty.post.model.post;


import java.time.LocalDateTime;


public interface CustomPost {
    String getId();
    String getContent();
    LocalDateTime getCreatedAt();
    String getUserId();
    Integer getCommentCount();
    String getMostCommentReacted();
    String getMostCommentReactedUser();
    Integer getHahaCount();
    Integer getAngryCount();
    Integer getSadCount();
    Integer getLoveCount();
    Integer getBurkCount();
}
