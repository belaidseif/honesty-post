package com.honesty.post.comment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.react.ReactEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReactCommentServiceTest {

    private ReactCommentService underTest;

    @Mock
    ReactCommentRepo repo;
    @Mock
    CommentService commentService;
    @Mock Post post;

    UUID userUid = UUID.randomUUID();


    String commentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    Comment comment = new Comment();

    @BeforeEach
    void setUp() {
        underTest = new ReactCommentService(commentService, repo);

        comment.setId(UUID.fromString(commentId));
        comment.setContent("first comment");
        comment.setPost(post);
        comment.setUserId(userUid);
    }

    @Test
    void should_add_react(){
        UUID uuid = UUID.randomUUID();
        given(commentService.getCommentById(commentId)).willReturn(comment);
        given(repo.findByCommentAndUserUid(comment, uuid)).willReturn(Optional.empty());

        underTest.addOrRemoveReact(commentId, ReactEnum.ANGRY ,uuid);

        ArgumentCaptor<ReactComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReactComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReactComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getComment().getId().toString(), commentId);
        assertEquals(captured.getReact(), ReactEnum.ANGRY);
        assertEquals(captured.getUserUid(),uuid);
    }

    @Test
    void should_replace_when_existing(){
        UUID uuid = UUID.randomUUID();
        ReactComment reactComment = new ReactComment();
        reactComment.setReact(ReactEnum.SAD);
        reactComment.setUserUid(uuid);

        given(commentService.getCommentById(commentId)).willReturn(comment);
        given(repo.findByCommentAndUserUid(comment, uuid)).willReturn(Optional.of(reactComment));
        underTest.addOrRemoveReact(commentId, ReactEnum.ANGRY ,uuid);

        ArgumentCaptor<ReactComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReactComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReactComment captured = userEntityArgumentCaptor.getValue();


        assertEquals(captured.getReact(), ReactEnum.ANGRY);
        assertEquals(captured.getUserUid(),uuid);
    }

    @Test
    void should_delete_react(){
        UUID uuid = UUID.randomUUID();
        ReactComment reactComment = new ReactComment();
        reactComment.setReact(ReactEnum.SAD);
        reactComment.setUserUid(uuid);

        given(commentService.getCommentById(commentId)).willReturn(comment);
        given(repo.findByCommentAndUserUid(comment, uuid)).willReturn(Optional.of(reactComment));
        underTest.addOrRemoveReact(commentId, ReactEnum.SAD ,uuid);


        verify(repo).delete(reactComment);

    }

}