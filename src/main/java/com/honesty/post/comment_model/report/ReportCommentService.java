package com.honesty.post.comment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.exception.CommentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportCommentService {

    private final ReportCommentRepo repo;
    private final CommentService commentService;


    public void reportCommentOfUser(String commentId, UUID userUid) {

        Comment comment = commentService.getCommentById(commentId);

        if(repo.existsByCommentAndUserUid(comment, userUid))
            throw new CommentException.SameAction("reported twice by same user");

        ReportComment report = new ReportComment();
        report.setComment(comment);
        report.setUserUid(userUid);

        repo.save(report);

    }
}
