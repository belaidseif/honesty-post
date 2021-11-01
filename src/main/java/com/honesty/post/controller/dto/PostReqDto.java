package com.honesty.post.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostReqDto {

    @NotNull
    @NotBlank
    private String content;

    private String Location;



}
