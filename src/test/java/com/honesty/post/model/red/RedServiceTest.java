package com.honesty.post.model.red;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.seen.Seen;
import com.honesty.post.model.seen.SeenRepo;
import com.honesty.post.model.seen.SeenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedServiceTest {

    @Mock
    PostService postService;
    @Mock
    RedRepo redRepo;

    private RedService underTest;

    String postId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID uuid = UUID.randomUUID();
    Post post = new Post();

    @BeforeEach
    void setUp() {
        underTest = new RedService(postService, redRepo);
        post.setId(UUID.fromString(postId));
        post.setUserId(uuid);
        post.setContent("post test");
        post.setIgnored(false);

    }

    @Test
    void should_add_red_to_post_when_it_is_not_red_by_user(){

        given(postService.getPostById(postId)).willReturn(post);
        given(redRepo.existsRedByPostIdAndUserUid(post.getId(), uuid)).willReturn(false);
        underTest.addRedByUserToPost(postId, uuid);

        ArgumentCaptor<Red> userEntityArgumentCaptor = ArgumentCaptor.forClass(Red.class);
        verify(redRepo).save(userEntityArgumentCaptor.capture());
        Red captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getPost().getId(), post.getId());
        assertEquals(captured.getPost().getUserId(), uuid);

    }

    @Test
    void should_not_add_red_to_post_when_it_is_already_red_by_user(){

        given(postService.getPostById(postId)).willReturn(post);
        given(redRepo.existsRedByPostIdAndUserUid(post.getId(), uuid)).willReturn(true);

        underTest.addRedByUserToPost(postId, uuid);


        verify(redRepo, never()).save(any());


    }

}