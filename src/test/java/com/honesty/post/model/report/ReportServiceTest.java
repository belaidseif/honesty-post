package com.honesty.post.model.report;

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
class ReportServiceTest {

    @Mock
    PostService postService;
    @Mock
    ReportRepo repo;

    private ReportService underTest;

    String postId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID uuid = UUID.randomUUID();
    Post post = new Post();

    @BeforeEach
    void setUp() {
        underTest = new ReportService(postService, repo);
        post.setId(UUID.fromString(postId));
        post.setUserId(uuid);
        post.setContent("post test");
        post.setIgnored(false);

    }

    @Test
    void should_report_post(){

        given(postService.getPostById(postId)).willReturn(post);
        given(repo.existsReportByPostIdAndUserUid(post.getId(), uuid)).willReturn(false);
        underTest.reportPost(postId, uuid);

        ArgumentCaptor<Report> userEntityArgumentCaptor = ArgumentCaptor.forClass(Report.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        Report captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getPost().getId(), post.getId());
        assertEquals(captured.getPost().getUserId(), uuid);

    }

    @Test
    void should_not_report_a_post_when_it_is_already_reported_by_user(){

        given(postService.getPostById(postId)).willReturn(post);
        given(repo.existsReportByPostIdAndUserUid(post.getId(), uuid)).willReturn(true);

        underTest.reportPost(postId, uuid);


        verify(repo, never()).save(any());


    }

}