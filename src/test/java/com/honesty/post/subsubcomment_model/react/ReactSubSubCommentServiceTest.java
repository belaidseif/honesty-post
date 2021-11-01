package com.honesty.post.subsubcomment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.react.ReactSubComment;
import com.honesty.post.subcomment_model.react.ReactSubCommentRepo;
import com.honesty.post.subcomment_model.react.ReactSubCommentService;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentService;
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
class ReactSubSubCommentServiceTest {


    private ReactSubSubCommentService underTest;

    @Mock
    ReactSubSubCommentRepo repo;
    @Mock
    SubSubCommentService subCommentService;
    @Mock
    SubComment comment;

    UUID userUid = UUID.randomUUID();


    String subCommentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    SubSubComment subComment = new SubSubComment();

    @BeforeEach
    void setUp() {
        underTest = new ReactSubSubCommentService(subCommentService, repo);

        subComment.setId(UUID.fromString(subCommentId));
        subComment.setContent("first comment");
        subComment.setSubComment(comment);
        subComment.setUserId(userUid);
    }

    @Test
    void should_add_react(){
        UUID uuid = UUID.randomUUID();
        given(subCommentService.getSubSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.findBySubSubCommentAndUserUid(subComment, uuid)).willReturn(Optional.empty());

        underTest.addOrRemoveReact(subCommentId, ReactEnum.ANGRY ,uuid);

        ArgumentCaptor<ReactSubSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReactSubSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReactSubSubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getSubSubComment().getId().toString(), subCommentId);
        assertEquals(captured.getReact(), ReactEnum.ANGRY);
        assertEquals(captured.getUserUid(),uuid);
    }

    @Test
    void should_replace_when_existing(){
        UUID uuid = UUID.randomUUID();
        ReactSubSubComment reactComment = new ReactSubSubComment();
        reactComment.setReact(ReactEnum.SAD);
        reactComment.setUserUid(uuid);

        given(subCommentService.getSubSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.findBySubSubCommentAndUserUid(subComment, uuid)).willReturn(Optional.of(reactComment));
        underTest.addOrRemoveReact(subCommentId, ReactEnum.ANGRY ,uuid);

        ArgumentCaptor<ReactSubSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReactSubSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReactSubSubComment captured = userEntityArgumentCaptor.getValue();


        assertEquals(captured.getReact(), ReactEnum.ANGRY);
        assertEquals(captured.getUserUid(),uuid);
    }

    @Test
    void should_delete_react(){
        UUID uuid = UUID.randomUUID();
        ReactSubSubComment reactComment = new ReactSubSubComment();
        reactComment.setReact(ReactEnum.SAD);
        reactComment.setUserUid(uuid);

        given(subCommentService.getSubSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.findBySubSubCommentAndUserUid(subComment, uuid)).willReturn(Optional.of(reactComment));
        underTest.addOrRemoveReact(subCommentId, ReactEnum.SAD ,uuid);



        verify(repo).delete(reactComment);

    }
}