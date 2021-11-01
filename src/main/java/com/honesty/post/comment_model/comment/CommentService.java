package com.honesty.post.comment_model.comment;

import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.controller.dto.CommentResDto;
import com.honesty.post.controller.dto.WrapperCommentResDto;
import com.honesty.post.exception.CommentException.CommentNotFound;
import com.honesty.post.exception.CommentException.NotSameUser;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.react.ReactEnum;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepo repo;
    private final PostService postService;
    public void addCommentToPost(String content, String location, UUID postUid, UUID userUid) {
        Post post = postService.getPostById(postUid.toString());

        Comment comment = new Comment();

        comment.setContent(content);
        comment.setLocation(location);
        comment.setUserId(userUid);
        comment.setPost(post);

        repo.save(comment);


    }

    public void ignoreCommentOfUser(String commentId, UUID userUid) {
        Comment comment = getCommentById(commentId);
        if(!comment.getUserId().equals(userUid))
            throw new NotSameUser("user has no authority form this comment");


        comment.setIgnored(true);
        repo.save(comment);
    }

    public Comment getCommentById(String id){
        UUID commentUid = null;
        try {
            commentUid = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            throw new CommentNotFound("comment not found");
        }

        Optional<Comment> byId = repo.findByIdAndIsIgnoredFalse(commentUid);
        Comment comment = byId.orElseThrow(()-> new CommentNotFound("comment not found"));

        return comment;
    }


    public WrapperCommentResDto getCommentAndReactsByPost(String postId,Integer page,Integer size, UUID userUid) {
        if(page <0 || size <0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"page or size cannot be less than zero");
        Post post = postService.getPostById(postId);
        List<Comment> comments = repo.findByPost(post, PageRequest.of(page, size));
        List<CommentResDto> collect = comments
                .stream()
                .map(comment -> getResDtoFromComment(comment, userUid))
                .collect(Collectors.toList());
        WrapperCommentResDto wrapper = new WrapperCommentResDto(collect, repo.countByPostAndIsIgnoredFalse(post));
        return wrapper;
    }

    private CommentResDto getResDtoFromComment(Comment comment,UUID userUid ){
        CommentResDto resDto = new CommentResDto(
                comment.getId(),
                comment.getContent(),
                comment.getLocation(),
                comment.getCreatedAt(),
                comment.getUserId()
        );

        if(userUid != null){
            List<ReactComment> collect = comment.getReactComments()
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
