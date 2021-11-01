package com.honesty.post.subcomment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.comment_model.report.ReportComment;
import com.honesty.post.comment_model.report.ReportCommentRepo;
import com.honesty.post.comment_model.report.ReportCommentService;
import com.honesty.post.exception.CommentException;
import com.honesty.post.model.post.Post;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
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
class ReportSubCommentServiceTest {


    private ReportSubCommentService underTest;

    @Mock
    ReportSubCommentRepo repo;
    @Mock
    SubCommentService subCommentService;
    @Mock
    Comment comment;

    UUID userUid = UUID.randomUUID();


    String subCommentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    SubComment subComment = new SubComment();

    @BeforeEach
    void setUp() {
        underTest = new ReportSubCommentService(subCommentService,repo );

        subComment.setId(UUID.fromString(subCommentId));
        subComment.setContent("first comment");
        subComment.setComment(comment);
        subComment.setUserId(userUid);
    }

    @Test
    void should_report_sub_comment(){
        given(subCommentService.getSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.existsBySubCommentAndUserUid(subComment, userUid)).willReturn(false);

        underTest.reportSubCommentOfUser(subCommentId, userUid);

        ArgumentCaptor<ReportSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReportSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReportSubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getSubComment().getId().toString(), subCommentId);
        assertEquals(captured.getUserUid(),userUid);
    }

    @Test
    void should_throw_exception_when_the_sub_comment_is_already_reported_by_user(){
        given(subCommentService.getSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.existsBySubCommentAndUserUid(subComment, userUid)).willReturn(true);

        assertThrows(
                CommentException.SameAction.class,
                ()-> underTest.reportSubCommentOfUser(subCommentId, userUid),
                "reported twice by same user"
        );

        verify(repo, never()).save(any());

    }
}