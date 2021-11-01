package com.honesty.post.subcomment_model.report;


import com.honesty.post.subcomment_model.subcomment.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportSubCommentRepo extends JpaRepository<ReportSubComment, UUID> {
    boolean existsBySubCommentAndUserUid(SubComment subComment, UUID userUid);
}
