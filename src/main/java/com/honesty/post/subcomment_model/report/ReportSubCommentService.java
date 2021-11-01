package com.honesty.post.subcomment_model.report;


import com.honesty.post.exception.CommentException;
import com.honesty.post.exception.CommentException.SameAction;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportSubCommentService {

    private final SubCommentService subCommentService;
    private final ReportSubCommentRepo repo;
    public void reportSubCommentOfUser(String subCommentId, UUID userUid) {
        SubComment subComment = subCommentService.getSubCommentById(subCommentId);

        if(repo.existsBySubCommentAndUserUid(subComment, userUid))
        throw new SameAction("sub comment already reported");

        ReportSubComment report = new ReportSubComment();
        report.setUserUid(userUid);
        report.setSubComment(subComment);

        repo.save(report);
    }
}
