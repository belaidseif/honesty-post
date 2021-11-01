package com.honesty.post.comment_model.react;


import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.model.react.ReactEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReactCommentService {

    private final CommentService commentService;
    private final ReactCommentRepo repo;

    public void addOrRemoveReact(String commentId, ReactEnum react, UUID userUid) {

        Comment comment = commentService.getCommentById(commentId);

        Optional<ReactComment> byCommentAndUserUid = repo.findByCommentAndUserUid(comment, userUid);

        if(byCommentAndUserUid.isPresent()){
            ReactComment reactComment = byCommentAndUserUid.get();
            if(reactComment.getReact().equals(react))
                repo.delete(reactComment);
            else{
                reactComment.setReact(react);
                repo.save(reactComment);
            }

        }else{
            ReactComment reactComment = new ReactComment();
            reactComment.setComment(comment);
            reactComment.setUserUid(userUid);
            reactComment.setReact(react);
            repo.save(reactComment);
        }
    }
}
