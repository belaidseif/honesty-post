package com.honesty.post.subsubcomment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.exception.CommentException;
import com.honesty.post.subcomment_model.report.ReportSubComment;
import com.honesty.post.subcomment_model.report.ReportSubCommentRepo;
import com.honesty.post.subcomment_model.report.ReportSubCommentService;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportSubSubCommentServiceTest {


    private ReportSubSubCommentService underTest;

    @Mock
    ReportSubSubCommentRepo repo;
    @Mock
    SubSubCommentService subCommentService;
    @Mock
    SubComment comment;

    UUID userUid = UUID.randomUUID();


    String subCommentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    SubSubComment subComment = new SubSubComment();

    @BeforeEach
    void setUp() {
        underTest = new ReportSubSubCommentService(repo, subCommentService);

        subComment.setId(UUID.fromString(subCommentId));
        subComment.setContent("first comment");
        subComment.setSubComment(comment);
        subComment.setUserId(userUid);
    }

    @Test
    void should_report_sub_comment(){
        given(subCommentService.getSubSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.existsBySubSubCommentAndUserUid(subComment, userUid)).willReturn(false);

        underTest.reportSubSubCommentOfUser(subCommentId, userUid);

        ArgumentCaptor<ReportSubSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(ReportSubSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        ReportSubSubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getSubSubComment().getId().toString(), subCommentId);
        assertEquals(captured.getUserUid(),userUid);
    }

    @Test
    void should_throw_exception_when_the_sub_comment_is_already_reported_by_user(){
        given(subCommentService.getSubSubCommentById(subCommentId)).willReturn(subComment);
        given(repo.existsBySubSubCommentAndUserUid(subComment, userUid)).willReturn(true);

        assertThrows(
                CommentException.SameAction.class,
                ()-> underTest.reportSubSubCommentOfUser(subCommentId, userUid),
                "reported twice by same user"
        );

        verify(repo, never()).save(any());

    }
}