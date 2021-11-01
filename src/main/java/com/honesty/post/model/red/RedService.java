package com.honesty.post.model.red;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.seen.Seen;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RedService {

    private final PostService postService;
    private final RedRepo repo;



        public void addRedByUserToPost(String postId, UUID userUid) {
            Post post = postService.getPostById(postId);
            Red red = new Red();
            red.setPost(post);
            red.setUserUid(userUid);
            if(!repo.existsRedByPostIdAndUserUid(post.getId(), userUid))
                repo.save(red);
        }


}
