package com.honesty.post.subsubcomment_model.report;


import com.honesty.post.exception.CommentException;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportSubSubCommentService {

    private final ReportSubSubCommentRepo repo;
    private final SubSubCommentService subSubCommentService;
    public void reportSubSubCommentOfUser(String subSubCommentId, UUID userUid) {
        SubSubComment subSubComment = subSubCommentService.getSubSubCommentById(subSubCommentId);

        if(repo.existsBySubSubCommentAndUserUid(subSubComment, userUid))
            throw new CommentException.SameAction("already reported");

        ReportSubSubComment report = new ReportSubSubComment();
        report.setSubSubComment(subSubComment);
        report.setUserUid(userUid);

        repo.save(report);

    }
}
