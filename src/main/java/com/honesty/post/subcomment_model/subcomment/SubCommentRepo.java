package com.honesty.post.subcomment_model.subcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.model.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubCommentRepo extends JpaRepository<SubComment, UUID> {

    List<SubComment> findByCommentAndIsIgnoredFalse(Comment comment, Pageable pageable);

    Integer countByCommentAndIsIgnoredFalse(Comment comment);

    Optional<SubComment> findByIdAndIsIgnoredFalse(UUID subCommentUid);

    Optional<SubComment> findByIdAndUserId(UUID id, UUID userUid);
}
