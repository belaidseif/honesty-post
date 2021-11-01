package com.honesty.post.model.react;



import com.honesty.post.model.post.Post;

import com.honesty.post.model.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ReactService {

    private final PostService postService;
    private final ReactRepo repo;

    public void addOrRemoveReact(ReactEnum react, String postId, UUID userUid) {

        Post post = postService.getPostById(postId);

        Optional<React> byUserIdAndPost = repo.findByUserUidAndPost(userUid, post);
        if(byUserIdAndPost.isPresent()){
            React savedReact = byUserIdAndPost.get();
            if(savedReact.getReact().equals(react))
                repo.delete(byUserIdAndPost.get());
            else {
                savedReact.setReact(react);
                repo.save(savedReact);
            }
        }else {
            React reactEntity = new React();
            reactEntity.setReact(react);
            reactEntity.setPost(post);
            reactEntity.setUserUid(userUid);
            repo.save(reactEntity);

        }
    }



}
