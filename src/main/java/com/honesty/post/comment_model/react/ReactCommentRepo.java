package com.honesty.post.comment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactCommentRepo extends JpaRepository<ReactComment, UUID> {

    Optional<ReactComment> findByCommentAndUserUid(Comment comment, UUID userUid);

}
