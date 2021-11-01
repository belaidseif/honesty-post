package com.honesty.post.controller.dto;

import com.honesty.post.model.post.Post;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class PostResDto {

    @NotNull
    private UUID id;
    @NotNull
    private String content;
    @NotNull
    private UUID userId;
    @NotNull
    private ZonedDateTime createdAt;
    private String location;

    public void createPostReqFromPost(Post post){
        content = post.getContent();
        createdAt = post.getCreatedAt();
        id = post.getId();
        location = post.getLocation();
        userId = post.getUserId();
    }

}
