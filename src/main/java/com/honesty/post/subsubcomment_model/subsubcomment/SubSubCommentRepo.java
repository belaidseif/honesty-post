package com.honesty.post.subsubcomment_model.subsubcomment;

import com.honesty.post.subcomment_model.subcomment.SubComment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubSubCommentRepo extends JpaRepository<SubSubComment, UUID> {
    List<SubSubComment> findBySubCommentAndIsIgnoredFalse(SubComment subComment, Pageable pageable);

    Integer countBySubCommentAndIsIgnoredFalse(SubComment subComment);

    Optional<SubSubComment> findByIdAndIsIgnoredFalse(UUID subSubCommentUid);
    Optional<SubSubComment> findByIdAndUserId(UUID id, UUID userUid);

}
