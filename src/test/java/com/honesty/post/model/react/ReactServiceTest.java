package com.honesty.post.model.react;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReactServiceTest {
    @Mock
    PostService postService;
    @Mock
    ReactRepo reactRepo;

    private ReactService underTest;

    String postId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID uuid = UUID.randomUUID();
    Post post = new Post();

    @BeforeEach
    void setUp() {
        underTest = new ReactService(postService, reactRepo);
        post.setId(UUID.fromString(postId));
        post.setUserId(uuid);
        post.setContent("post test");
        post.setIgnored(false);

    }

    @Test
    void should_add_react(){

        given(postService.getPostById(postId)).willReturn(post);
        given(reactRepo.findByUserUidAndPost(uuid, post)).willReturn(Optional.empty());
        underTest.addOrRemoveReact(ReactEnum.ANGRY, postId, uuid);

        ArgumentCaptor<React> userEntityArgumentCaptor = ArgumentCaptor.forClass(React.class);
        verify(reactRepo).save(userEntityArgumentCaptor.capture());
        React capturedUserEntity = userEntityArgumentCaptor.getValue();

        assertEquals(capturedUserEntity.getReact(), ReactEnum.ANGRY);
        assertEquals(capturedUserEntity.getUserUid(), uuid);
        assertEquals(capturedUserEntity.getPost(), post);
    }

    @Test
    void should_delete_react_when_it_is_the_same(){
        React react = new React();
        react.setReact(ReactEnum.ANGRY);
        react.setPost(post);
        react.setUserUid(uuid);
        given(postService.getPostById(postId)).willReturn(post);
        given(reactRepo.findByUserUidAndPost(uuid, post)).willReturn(Optional.of(react));

        underTest.addOrRemoveReact(ReactEnum.ANGRY, postId, uuid);

        verify(reactRepo).delete(react);
    }

    @Test
    void should_replace_the_existing_react_by_the_new_one(){
        React react = new React();
        react.setReact(ReactEnum.SAD);
        react.setPost(post);
        react.setUserUid(uuid);
        given(postService.getPostById(postId)).willReturn(post);
        given(reactRepo.findByUserUidAndPost(uuid, post)).willReturn(Optional.of(react));

        underTest.addOrRemoveReact(ReactEnum.ANGRY, postId, uuid);

        ArgumentCaptor<React> userEntityArgumentCaptor = ArgumentCaptor.forClass(React.class);
        verify(reactRepo).save(userEntityArgumentCaptor.capture());
        React capturedUserEntity = userEntityArgumentCaptor.getValue();

        assertEquals(capturedUserEntity.getReact(), ReactEnum.ANGRY);

    }
}