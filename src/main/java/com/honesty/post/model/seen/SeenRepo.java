package com.honesty.post.model.seen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface SeenRepo extends JpaRepository<Seen, UUID> {

    boolean existsSeenByPostIdAndUserUid(UUID postUid, UUID userUid);
}
