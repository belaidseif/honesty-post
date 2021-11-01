package com.honesty.post.controller.dto.res;

import com.honesty.post.controller.dto.CommentResDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
public class WrapperSubCommentResDto {
    @NotNull
    private List<SubCommentResDto> subCommentResDtos;
    @NotNull
    private Integer count;
}
