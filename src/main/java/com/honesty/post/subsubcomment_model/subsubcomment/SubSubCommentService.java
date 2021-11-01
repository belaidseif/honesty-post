package com.honesty.post.subsubcomment_model.subsubcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.controller.dto.res.SubCommentResDto;
import com.honesty.post.controller.dto.res.SubSubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubSubCommentResDto;
import com.honesty.post.exception.CommentException;

import com.honesty.post.exception.SubSubCommentException.SubSubCommentNotFound;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.react.ReactSubComment;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import com.honesty.post.subsubcomment_model.react.ReactSubSubComment;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubSubCommentService {

    private final SubCommentService subCommentService;
    private final SubSubCommentRepo repo;

    public void addSubSubCommentToComment(String content, String location, UUID subCommentUid, UUID userUid) {
        SubComment subComment = subCommentService.getSubCommentById(subCommentUid.toString());

        SubSubComment subSubComment = new SubSubComment();

        subSubComment.setSubComment(subComment);
        subSubComment.setContent(content);
        subSubComment.setLocation(location);
        subSubComment.setUserId(userUid);

        repo.save(subSubComment);

    }

    public void ignoreSubSubCommentOfUser(String subSubCommentId, UUID userUid) {

        SubSubComment subSubComment = getSubSubCommentById(subSubCommentId);

        if(!subSubComment.getUserId().equals(userUid))
            throw new CommentException.NotSameUser("user has no authority to this sub comment");

        if(subSubComment.isIgnored() == true)
            throw new CommentException.SameAction("already ignored");

        subSubComment.setIgnored(true);
        repo.save(subSubComment);
    }

    public SubSubComment getSubSubCommentById(String id) {
        UUID subSubCommentUid = null;
        try {
            subSubCommentUid = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            throw new SubSubCommentNotFound("sub sub comment not found");
        }

        Optional<SubSubComment> byId = repo.findByIdAndIsIgnoredFalse(subSubCommentUid);
        SubSubComment subSubComment = byId.orElseThrow(
                ()-> new SubSubCommentNotFound("ub sub comment not founds")
        );

        return subSubComment;
    }

    public WrapperSubSubCommentResDto getSubSubCommentsBySubCommentId(
            String subCommentId,
            Integer page,
            Integer size,
            UUID userUid
    ) {
        if(page <0 || size <0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"page or size cannot be less than zero");

        SubComment subComment = subCommentService.getSubCommentById(subCommentId);

        List<SubSubComment> subSubComments = repo.findBySubCommentAndIsIgnoredFalse(subComment, PageRequest.of(page, size));

        List<SubSubCommentResDto> collect = subSubComments
                .stream()
                .map(subSubComment -> getResDtoFromSubSubComment(subSubComment, userUid))
                .collect(Collectors.toList());

        WrapperSubSubCommentResDto wrapper = new WrapperSubSubCommentResDto(collect, repo.countBySubCommentAndIsIgnoredFalse(subComment));
        return wrapper;
    }

    private SubSubCommentResDto getResDtoFromSubSubComment(SubSubComment subSubComment, UUID userUid ){
        SubSubCommentResDto resDto = new SubSubCommentResDto(
                subSubComment.getId(),
                subSubComment.getContent(),
                subSubComment.getLocation(),
                subSubComment.getCreatedAt(),
                subSubComment.getUserId()
        );

        if(userUid != null){
            List<ReactSubSubComment> collect = subSubComment.getReactComments()
                    .stream()
                    .filter(react -> react.getUserUid().equals(userUid))
                    .collect(Collectors.toList());

            if(collect.size() > 0)
                resDto.setClientReact(collect.get(0).getReact());
        }
        subSubComment.getReactComments().forEach(react -> {
            ReactEnum key = react.getReact();
            resDto.getReacts().put(key, resDto.getReacts().getOrDefault(key,0)+1);
        });

        return resDto;
    }
}
