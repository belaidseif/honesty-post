package com.honesty.post.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
public class WrapperCommentResDto {
    @NotNull
    private List<CommentResDto> commentResDtos;
    @NotNull
    private Integer count;
}
