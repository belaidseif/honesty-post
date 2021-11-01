package com.honesty.post.controller;

import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.controller.dto.CustomPostResDto;
import com.honesty.post.controller.dto.PostReqDto;
import com.honesty.post.controller.dto.PostResDto;
import com.honesty.post.model.post.CustomPost;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("post-api/post")
@AllArgsConstructor
@Tag(name = "post api")
public class PostController {

    private final PostService postService;


    @PostMapping("add")
    @PreAuthorize("hasAuthority('post:write')")
    @Operation(description = "400: bad request")
    public ResponseEntity<String> addNewPost(@RequestBody @Valid PostReqDto postReqDto, HttpServletRequest request){
        postService.addNewPost(postReqDto.getContent(), postReqDto.getLocation(), getUserUid(request));
        return ResponseEntity.status(HttpStatus.CREATED).body("post added");
    }

    @PostMapping("ignore/{id}")
    @PreAuthorize("hasAuthority('post:write')")
    @Operation(description = "400: bad request\n404: post not found\n 406: user has no authorities")
    public ResponseEntity<String> ignorePost(@PathVariable("id") String postId,HttpServletRequest request){

        postService.ignorePost(postId, getUserUid(request));
        return ResponseEntity.status(HttpStatus.CREATED).body("post ignored");
    }

    @GetMapping("definition/{id}")
    @Operation(description = "404: post not found")
    public ResponseEntity<CustomPostResDto> getDefinitionOfPostById(@PathVariable("id") String postId){

        CustomPost customPost =postService.getCustomPostById(postId);


        CustomPostResDto resDto = new CustomPostResDto(
                customPost.getId(),
                customPost.getCommentCount(),
                customPost.getMostCommentReacted(),
                customPost.getMostCommentReactedUser(),
                customPost.getHahaCount(),
                customPost.getAngryCount(),
                customPost.getSadCount(),
                customPost.getLoveCount(),
                customPost.getBurkCount()
        );

        return ResponseEntity.ok(resDto);
    }

    @GetMapping("posts/{id}")
    @Operation(description = "404: post not found")
    public ResponseEntity<PostResDto> getPostById(@PathVariable("id") String postId){
        PostResDto postRes = new PostResDto();
        Post post = postService.getPostById(postId);
        postRes.createPostReqFromPost(post);

        return ResponseEntity.ok(postRes);
    }



    @GetMapping()
    public ResponseEntity<List<PostResDto>> getPostsByLocation(@RequestParam(required = false) String location, HttpServletRequest request){
        List<Post> posts = postService.getPostsByLocation(location);

        List<PostResDto> postsRes = posts.stream().map(post -> {
            PostResDto postRes = new PostResDto();
            postRes.createPostReqFromPost(post);
            return postRes;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(postsRes) ;

    }




    private UUID getUserUid( HttpServletRequest request){
        String username = String.valueOf(request.getAttribute("username"));
        System.out.println("username " + username);
        if(!username.equals("null"))
            return UUID.fromString(username);
        return null;
    }
}
