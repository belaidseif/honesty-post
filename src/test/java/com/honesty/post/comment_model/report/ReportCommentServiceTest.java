package com.honesty.post.comment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.comment_model.react.ReactCommentRepo;
import com.honesty.post.comment_model.react.ReactCommentService;
import com.honesty.post.exception.CommentException;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.react.ReactEnum;
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
class ReportCommentServiceTest {

    private ReportCommentService underTest;

    @Mock
    ReportCommentRepo repo;
    @Mock
    CommentService commentService;
    @Mock
    Post post;

    UUID userUid = UUID.randomUUID();


    String commentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    Comment comment = new Comment();

    @BeforeEach
    void setUp() {
        underTest = new ReportCommentService(repo,commentService );

        comment.setId(UUID.fromString(commentId));
        comment.setContent("first comment");
        comment.setPost(post);
        comment.setUserId(userUid);
    }

    @Test
    void should_report_comment(){
        given(commentService.getCommentById(commentId)).willReturn(comment);
        given(repo.existsByCommentAndUserUid(comment, userUid)).willReturn(false);

        underTest.reportCommentOfUser(commentId, userUid);

        ArgumentCaptor<ReportComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReportComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReportComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getComment().getId().toString(), commentId);
        assertEquals(captured.getUserUid(),userUid);
    }

    @Test
    void should_throw_exception_when_the_comment_is_already_reported_by_user(){
        given(commentService.getCommentById(commentId)).willReturn(comment);
        given(repo.existsByCommentAndUserUid(comment, userUid)).willReturn(true);

        assertThrows(
                CommentException.SameAction.class,
                ()-> underTest.reportCommentOfUser(commentId, userUid),
                "reported twice by same user"
                );

        verify(repo, never()).save(any());

    }
}