package com.honesty.post.subcomment_model.subcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.controller.dto.res.SubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.exception.CommentException;
import com.honesty.post.exception.SubCommentException;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.react.ReactSubComment;
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
public class SubCommentService {

    private final SubCommentRepo repo;
    private final CommentService commentService;


    public void addSubCommentToComment(String content, String location, UUID commentUid, UUID userUid) {
        Comment comment = commentService.getCommentById(commentUid.toString());

        SubComment subComment = new SubComment();
        subComment.setContent(content);
        subComment.setLocation(location);
        subComment.setComment(comment);
        subComment.setUserId(userUid);

        repo.save(subComment);
    }

    public void ignoreSubCommentOfUser(String subCommentId, UUID userUid) {
        SubComment subComment = getSubCommentById(subCommentId);

        if(!subComment.getUserId().equals(userUid))
            throw new CommentException.NotSameUser("user has no authority to this sub comment");

        if(subComment.isIgnored() == true)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        subComment.setIgnored(true);
        repo.save(subComment);
    }

    public SubComment getSubCommentById(String id){
        UUID subCommentUid = null;
        try {
            subCommentUid = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            throw new SubCommentException.SubCommentNotFound("sub comment not found");
        }

        Optional<SubComment> byId = repo.findByIdAndIsIgnoredFalse(subCommentUid);
        SubComment subComment = byId.orElseThrow(
                ()-> new SubCommentException.SubCommentNotFound("sub comment not found")
        );

        return subComment;
    }

    public WrapperSubCommentResDto getSubCommentsByCommentId(String commentId, Integer page, Integer size, UUID userUid) {
        if(page <0 || size <0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"page or size cannot be less than zero");

        Comment comment = commentService.getCommentById(commentId);

        List<SubComment> subComments = repo.findByCommentAndIsIgnoredFalse(comment, PageRequest.of(page, size));

        List<SubCommentResDto> collect = subComments
                .stream()
                .map(subComment -> getResDtoFromSubComment(subComment, userUid))
                .collect(Collectors.toList());

        WrapperSubCommentResDto wrapper = new WrapperSubCommentResDto(collect, repo.countByCommentAndIsIgnoredFalse(comment));
        return wrapper;

    }

    private SubCommentResDto getResDtoFromSubComment(SubComment comment, UUID userUid ){
        SubCommentResDto resDto = new SubCommentResDto(
                comment.getId(),
                comment.getContent(),
                comment.getLocation(),
                comment.getCreatedAt(),
                comment.getUserId()
        );

        if(userUid != null){
            List<ReactSubComment> collect = comment.getReactComments()
                    .stream()
                    .filter(react -> react.getUserUid().equals(userUid))
                    .collect(Collectors.toList());

            if(collect.size() > 0)
                resDto.setClientReact(collect.get(0).getReact());
        }
        comment.getReactComments().forEach(react -> {
            ReactEnum key = react.getReact();
            resDto.getReacts().put(key, resDto.getReacts().getOrDefault(key,0)+1);
        });

        return resDto;
    }
}
