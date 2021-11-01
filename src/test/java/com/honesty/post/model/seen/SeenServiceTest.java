package com.honesty.post.model.seen;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.react.React;
import com.honesty.post.model.react.ReactRepo;
import com.honesty.post.model.react.ReactService;
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
class SeenServiceTest {

    @Mock
    PostService postService;
    @Mock
    SeenRepo seenRepo;

    private SeenService underTest;

    String postId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID uuid = UUID.randomUUID();
    Post post = new Post();

    @BeforeEach
    void setUp() {
        underTest = new SeenService(postService, seenRepo);
        post.setId(UUID.fromString(postId));
        post.setUserId(uuid);
        post.setContent("post test");
        post.setIgnored(false);

    }

    @Test
    void should_add_seen_to_post_when_it_is_not_seen_by_user(){

        given(postService.getPostById(postId)).willReturn(post);
        given(seenRepo.existsSeenByPostIdAndUserUid(post.getId(), uuid)).willReturn(false);
        underTest.addSeenByUserToPost(postId, uuid);
        ArgumentCaptor<Seen> userEntityArgumentCaptor = ArgumentCaptor.forClass(Seen.class);
        verify(seenRepo).save(userEntityArgumentCaptor.capture());
        Seen captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getPost().getId(), post.getId());
        assertEquals(captured.getPost().getUserId(), uuid);

    }

    @Test
    void should_not_add_seen_to_post_when_it_is_already_seen_by_user(){

        given(postService.getPostById(postId)).willReturn(post);
        given(seenRepo.existsSeenByPostIdAndUserUid(post.getId(), uuid)).willReturn(true);

        underTest.addSeenByUserToPost(postId, uuid);


        verify(seenRepo, never()).save(any());


    }

}