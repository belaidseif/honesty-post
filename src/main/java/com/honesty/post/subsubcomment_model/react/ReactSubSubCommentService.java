package com.honesty.post.subsubcomment_model.react;

import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReactSubSubCommentService {

    private final SubSubCommentService subSubCommentService;
    private final ReactSubSubCommentRepo repo;

    public void addOrRemoveReact(String subSubCommentId, ReactEnum react, UUID userUid) {
        SubSubComment subSubComment = subSubCommentService.getSubSubCommentById(subSubCommentId);

        Optional<ReactSubSubComment> reactOptional = repo.findBySubSubCommentAndUserUid(subSubComment, userUid);

        if(reactOptional.isPresent()){
            ReactSubSubComment reactSubSubComment = reactOptional.get();
            if(reactSubSubComment.getReact().equals(react))
                repo.delete(reactSubSubComment);
            else{
                reactSubSubComment.setReact(react);
                repo.save(reactSubSubComment);
            }
        }else {
            ReactSubSubComment reactSubSubComment = new ReactSubSubComment();
            reactSubSubComment.setReact(react);
            reactSubSubComment.setUserUid(userUid);
            reactSubSubComment.setSubSubComment(subSubComment);

            repo.save(reactSubSubComment);
        }
    }
}
