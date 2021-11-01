package com.honesty.post.model.post;

import com.honesty.post.exception.CommentException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static com.honesty.post.exception.PostException.*;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepo repo;


    public void addNewPost(String content, String location, UUID userUid) {
        Post post = new Post();
        post.setContent(content);
        if(location != null)
            post.setLocation(location);
        post.setUserId(userUid);
        post.setCreatedAt(ZonedDateTime.now());
        post.setIgnored(false);
        repo.save(post);
    }

    public void ignorePost(String postId, UUID userUid) {

        Post post = getPostById(postId);
        if(!post.getUserId().equals(userUid))
            throw new CommentException.NotSameUser("user has no authorities");

        post.setIgnored(true);
        repo.save(post);

    }

    public Post getPostById(String postId) {
        UUID postUid = null;
        try {
           postUid = UUID.fromString(postId);
        }catch (IllegalArgumentException e){
            throw new PostNotFound("post not found");
        }

        Optional<Post> byId = repo.findByIdAndIsIgnoredFalse(postUid);
        Post post = byId.orElseThrow(()-> new PostNotFound("post not found"));

        return post;



    }

    public List<Post> getPostsByLocation(String location) {

        if(location == null) {
           return repo.findAll();
        } else
           return repo.findByLocationAndIsIgnoredFalseLimitedTo(location,3);

    }

    public CustomPost getCustomPostById(String postId) {
        UUID postUid = getUuidFromString(postId);

        Optional<CustomPost> byId = repo.findCustomPostById(postUid);
        CustomPost customPost = byId.orElseThrow(()-> new PostNotFound("post not found"));

        return customPost;
    }


    private UUID getUuidFromString(String id){
        UUID postUid = null;
        try {
            postUid = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            throw new PostNotFound("post not found");
        }
        return postUid;
    }
}
