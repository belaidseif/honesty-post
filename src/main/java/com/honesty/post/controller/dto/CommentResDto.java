package com.honesty.post.controller.dto;

import com.honesty.post.model.react.ReactEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class CommentResDto {

    @NotNull
    private UUID id;
    @NotNull
    private String content;
    private String location;
    @NotNull
    private ZonedDateTime createdAt;
    @NotNull
    private UUID userId;
    @NotNull
    Map<ReactEnum, Integer> reacts;
    private ReactEnum clientReact;

    public CommentResDto(UUID id, String content, String location, ZonedDateTime createdAt, UUID userId) {
        this.id = id;
        this.content = content;
        this.location = location;
        this.createdAt = createdAt;
        this.userId = userId;
        reacts = new HashMap<>();
    }
}
