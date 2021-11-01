package com.honesty.post.model.seen;

import com.honesty.post.exception.PostException;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SeenService {

    private final PostService postService;
    private final SeenRepo repo;
    public void addSeenByUserToPost(String postId, UUID userUid) {
        Post post = postService.getPostById(postId);
        Seen seen = new Seen();
        seen.setPost(post);
        seen.setUserUid(userUid);
        if(!repo.existsSeenByPostIdAndUserUid(post.getId(), userUid))
            repo.save(seen);
    }


}
