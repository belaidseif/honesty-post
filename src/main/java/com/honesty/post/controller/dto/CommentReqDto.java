package com.honesty.post.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CommentReqDto {
    @NotNull
    @NotBlank
    private String content;

    private String Location;

    @NotNull
    private UUID postUid;
}
