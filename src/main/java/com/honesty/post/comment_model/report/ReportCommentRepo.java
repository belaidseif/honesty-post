package com.honesty.post.comment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportCommentRepo extends JpaRepository<ReportComment, UUID> {

    boolean existsByCommentAndUserUid(Comment comment, UUID userUid);
}
