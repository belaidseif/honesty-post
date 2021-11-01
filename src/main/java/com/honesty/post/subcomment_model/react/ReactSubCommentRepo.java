package com.honesty.post.subcomment_model.react;

import com.honesty.post.subcomment_model.subcomment.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactSubCommentRepo extends JpaRepository<ReactSubComment, UUID> {
    Optional<ReactSubComment> findBySubCommentAndUserUid(SubComment subComment, UUID userUid);
}
