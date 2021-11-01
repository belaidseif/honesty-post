package com.honesty.post.model.red;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RedRepo extends JpaRepository<Red, UUID> {

    boolean existsRedByPostIdAndUserUid(UUID postId, UUID userUid);
}
