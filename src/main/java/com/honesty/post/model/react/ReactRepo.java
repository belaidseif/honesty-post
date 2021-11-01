package com.honesty.post.model.react;

import com.honesty.post.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactRepo extends JpaRepository<React, UUID> {

    Optional<React> findByUserUidAndPost(UUID userId, Post post);



}
