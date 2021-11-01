package com.honesty.post.subcomment_model.react;

import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReactSubCommentService {

    private final SubCommentService subCommentService;
    private final ReactSubCommentRepo repo;

    public void addOrRemoveReact(String subCommentId, ReactEnum react, UUID userUid) {
        SubComment subComment = subCommentService.getSubCommentById(subCommentId);

        Optional<ReactSubComment> reactSubCommentOptional = repo.findBySubCommentAndUserUid(subComment, userUid);

        if(reactSubCommentOptional.isPresent()){
            ReactSubComment reactSubComment = reactSubCommentOptional.get();
            if(reactSubComment.getReact().equals(react))
                repo.delete(reactSubComment);
            else{
                reactSubComment.setReact(react);
                repo.save(reactSubComment);
            }
        }else {
            ReactSubComment reactSubComment = new ReactSubComment();
            reactSubComment.setSubComment(subComment);
            reactSubComment.setUserUid(userUid);
            reactSubComment.setReact(react);

            repo.save(reactSubComment);
        }



    }
}
