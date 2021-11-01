package com.honesty.post.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class CustomPostResDto {

    @NotNull
    private String id;


    @NotNull
    private Integer commentCount;

    private String mostCommentReacted;

    private String mostCommentReactedUser;

    @NotNull
    private Integer hahaCount;

    @NotNull
    private Integer angryCount;

    @NotNull
    private Integer sadCount;

    @NotNull
    private Integer loveCount;

    @NotNull
    private Integer burkCount;
}
