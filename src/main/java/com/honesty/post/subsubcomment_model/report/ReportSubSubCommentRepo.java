package com.honesty.post.subsubcomment_model.report;

import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportSubSubCommentRepo extends JpaRepository<ReportSubSubComment, UUID> {
    boolean existsBySubSubCommentAndUserUid(SubSubComment subSubComment, UUID userUid);
}
