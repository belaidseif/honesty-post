package com.honesty.post.comment_model.comment;

import com.honesty.post.model.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepo extends PagingAndSortingRepository<Comment, UUID> {

    Optional<Comment> findByIdAndUserId(UUID id, UUID userUid);

    List<Comment> findByPost(Post post, Pageable pageable);

    Integer countByPostAndIsIgnoredFalse(Post post);

    Optional<Comment> findByIdAndIsIgnoredFalse(UUID commentUid);
}
