package com.honesty.post.controller.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
public class WrapperSubSubCommentResDto {
    @NotNull
    private List<SubSubCommentResDto> subSubCommentResDtos;
    @NotNull
    private Integer count;
}
