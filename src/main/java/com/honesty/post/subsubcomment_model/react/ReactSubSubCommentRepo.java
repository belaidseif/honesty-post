package com.honesty.post.subsubcomment_model.react;

import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactSubSubCommentRepo extends JpaRepository<ReactSubSubComment, UUID> {
    Optional<ReactSubSubComment> findBySubSubCommentAndUserUid(SubSubComment subSubComment, UUID userUid);
}
