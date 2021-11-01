package com.honesty.post.controller;


import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.comment_model.react.ReactCommentService;
import com.honesty.post.comment_model.report.ReportCommentService;
import com.honesty.post.controller.dto.CommentReqDto;

import com.honesty.post.controller.dto.WrapperCommentResDto;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("post-api/comment")
@AllArgsConstructor
@Tag(name = "comment api")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ReportCommentService reportCommentService;
    private final ReactCommentService reactCommentService;


    @PostMapping()
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: post not found")
    public ResponseEntity<String> addComment(@RequestBody @Valid CommentReqDto comment, HttpServletRequest request){

        commentService.addCommentToPost(
                comment.getContent(),
                comment.getLocation(),
                comment.getPostUid(),
                userService.getUserUid(request)
        );
        return ResponseEntity.status(201).body("comment added");
    }

    @PostMapping("ignore/{commentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: comment not found\n406: not same user")
    public ResponseEntity<String> ignoreComment(@PathVariable String commentId,HttpServletRequest request){
        commentService.ignoreCommentOfUser(commentId, userService.getUserUid(request));
        return ResponseEntity.status(201).body("comment ignored");
    }


    @PostMapping("report/{commentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: comment not found\n406: reported twice")
    public ResponseEntity<String> reportComment(@PathVariable String commentId,HttpServletRequest request){

        reportCommentService.reportCommentOfUser(commentId, userService.getUserUid(request));
        return ResponseEntity.status(201).body("comment reported");
    }


    @PostMapping("react/{commentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: comment not found")
    public ResponseEntity<String> reactToComment(@RequestParam ReactEnum react, @PathVariable String commentId, HttpServletRequest request){

        reactCommentService.addOrRemoveReact(commentId, react, userService.getUserUid(request));
        return ResponseEntity.status(201).body("react added");
    }



    @GetMapping("by-post/{postId}")
    @Operation(description = "404: post not found")
    public ResponseEntity<WrapperCommentResDto> getCommentByPost(
            @PathVariable String postId,
            @RequestParam Integer page,
            @RequestParam Integer size,
            HttpServletRequest request
            ){
        WrapperCommentResDto commentsByPost = commentService.getCommentAndReactsByPost(
                postId,
                page,
                size,
                userService.getUserUid(request));
        return ResponseEntity.ok(commentsByPost);
    }
}
