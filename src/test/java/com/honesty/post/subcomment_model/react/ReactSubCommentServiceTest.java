package com.honesty.post.subcomment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.comment_model.react.ReactCommentRepo;
import com.honesty.post.comment_model.react.ReactCommentService;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
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
class ReactSubCommentServiceTest {


    private ReactSubCommentService underTest;

    @Mock
    ReactSubCommentRepo repo;
    @Mock
    SubCommentService subCommentService;
    @Mock
    Comment comment;

    UUID userUid = UUID.randomUUID();


    String subCommentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    SubComment subComment = new SubComment();

    @BeforeEach
    void setUp() {
        underTest = new ReactSubCommentService(subCommentService, repo);

        subComment.setId(UUID.fromString(subCommentId));
        subComment.setContent("first comment");
        subComment.setComment(comment);
        subComment.setUserId(userUid);
    }

    @Test
    void should_add_react(){
        UUID uuid = UUID.randomUUID();
        given(subCommentService.getSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.findBySubCommentAndUserUid(subComment, uuid)).willReturn(Optional.empty());

        underTest.addOrRemoveReact(subCommentId, ReactEnum.ANGRY ,uuid);

        ArgumentCaptor<ReactSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReactSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReactSubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getSubComment().getId().toString(), subCommentId);
        assertEquals(captured.getReact(), ReactEnum.ANGRY);
        assertEquals(captured.getUserUid(),uuid);
    }

    @Test
    void should_replace_when_existing(){
        UUID uuid = UUID.randomUUID();
        ReactSubComment reactComment = new ReactSubComment();
        reactComment.setReact(ReactEnum.SAD);
        reactComment.setUserUid(uuid);

        given(subCommentService.getSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.findBySubCommentAndUserUid(subComment, uuid)).willReturn(Optional.of(reactComment));
        underTest.addOrRemoveReact(subCommentId, ReactEnum.ANGRY ,uuid);

        ArgumentCaptor<ReactSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReactSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReactSubComment captured = userEntityArgumentCaptor.getValue();


        assertEquals(captured.getReact(), ReactEnum.ANGRY);
        assertEquals(captured.getUserUid(),uuid);
    }

    @Test
    void should_delete_react(){
        UUID uuid = UUID.randomUUID();
        ReactSubComment reactComment = new ReactSubComment();
        reactComment.setReact(ReactEnum.SAD);
        reactComment.setUserUid(uuid);

        given(subCommentService.getSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.findBySubCommentAndUserUid(subComment, uuid)).willReturn(Optional.of(reactComment));
        underTest.addOrRemoveReact(subCommentId, ReactEnum.SAD ,uuid);



        verify(repo).delete(reactComment);

    }
}