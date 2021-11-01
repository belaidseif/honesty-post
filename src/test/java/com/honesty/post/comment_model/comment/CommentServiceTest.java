package com.honesty.post.comment_model.comment;

import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.controller.dto.WrapperCommentResDto;
import com.honesty.post.exception.CommentException;
import com.honesty.post.model.post.Post;

import com.honesty.post.model.post.PostService;
import com.honesty.post.model.react.ReactEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private CommentService underTest;

    @Mock
    CommentRepo repo;
    @Mock
    PostService postService;

    String postId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID userUid = UUID.randomUUID();
    Post post = new Post();

    String commentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    Comment comment = new Comment();

    @BeforeEach
    void setUp() {
        underTest = new CommentService(repo, postService);
        post.setId(UUID.fromString(postId));
        post.setUserId(userUid);
        post.setContent("post test");

        comment.setId(UUID.fromString(commentId));
        comment.setContent("first comment");
        comment.setPost(post);
        comment.setUserId(userUid);
    }

    @Test
    void should_add_comment_to_post(){
        given(postService.getPostById(postId)).willReturn(post);

        underTest.addCommentToPost("comment test", "paris", UUID.fromString(postId), userUid);

        ArgumentCaptor<Comment> userEntityArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        Comment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getContent(), "comment test");
        assertEquals(captured.getLocation(), "paris");
        assertEquals(captured.getUserId(), userUid);
        assertEquals(captured.getPost().getId().toString(), postId);

    }

    @Test
    void should_ignore_comment(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(commentId))).willReturn(Optional.of(comment));

        underTest.ignoreCommentOfUser( commentId, userUid);

        ArgumentCaptor<Comment> userEntityArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        Comment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getContent(), "first comment");
        assertEquals(captured.getUserId(), userUid);
        assertEquals(captured.isIgnored(), true);
        assertEquals(captured.getPost().getId().toString(), postId);

    }

    @Test
    void should_throw_comment_not_found(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(commentId))).willReturn(Optional.empty());

        assertThrows(CommentException.CommentNotFound.class,
                ()-> underTest.ignoreCommentOfUser( commentId, userUid),
                "comment not found"
        );

    }
    @Test
    void should_throw_comment_not_found_when_commentId_is_not_castable_to_UUID(){

        assertThrows(CommentException.CommentNotFound.class,
                ()-> underTest.ignoreCommentOfUser( "1234", userUid),
                "comment not found"
        );

    }

    @Test
    void should_throw_not_same_user(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(commentId))).willReturn(Optional.of(comment));

        assertThrows(CommentException.NotSameUser.class,
                ()-> underTest.ignoreCommentOfUser( commentId, UUID.randomUUID()),
                "user has no authority form this comment"
        );

    }

    @Test
    void should_return_comment_with_react_by_post(){
        given(postService.getPostById(postId)).willReturn(post);
        comment.setReactComments(Arrays.asList());
        Comment comment1 = new Comment();
        comment1.setPost(post);
        comment1.setContent("comment 2");
        comment1.setUserId(userUid);

        ReactComment reactComment = new ReactComment();
        reactComment.setReact(ReactEnum.HAHA);
        reactComment.setUserUid(UUID.randomUUID());

        ReactComment reactComment2 = new ReactComment();
        reactComment2.setReact(ReactEnum.SAD);
        reactComment2.setUserUid(UUID.randomUUID());

        ReactComment reactComment3 = new ReactComment();
        reactComment3.setReact(ReactEnum.LOVE);
        reactComment3.setUserUid(userUid);

        comment1.setReactComments(Arrays.asList(reactComment, reactComment2, reactComment3));

        given(repo.findByPost(post, PageRequest.of(0, 10)))
                .willReturn(Arrays.asList(comment, comment1));
        given(repo.countByPostAndIsIgnoredFalse(post)).willReturn(5);

        WrapperCommentResDto wrapper = underTest.getCommentAndReactsByPost(postId, 0, 10, userUid);

        assertEquals(wrapper.getCount(), 5);
        assertEquals(wrapper.getCommentResDtos().get(0).getContent(), "first comment");
        assertEquals(wrapper.getCommentResDtos().get(1).getContent(), "comment 2");
        assertEquals(wrapper.getCommentResDtos().get(1).getReacts().get(ReactEnum.HAHA), 1);
        assertEquals(wrapper.getCommentResDtos().get(1).getClientReact(), ReactEnum.LOVE);

    }

    @Test
    void should_throw_bad_request(){
        assertThrows(
                ResponseStatusException.class,
                ()-> underTest.getCommentAndReactsByPost(postId, -1,10,userUid),
                "page or size cannot be less than zero"
                );
    }
}