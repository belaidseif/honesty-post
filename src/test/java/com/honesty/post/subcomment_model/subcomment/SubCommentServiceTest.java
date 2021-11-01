package com.honesty.post.subcomment_model.subcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.controller.dto.WrapperCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.exception.CommentException;
import com.honesty.post.exception.SubCommentException;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.react.ReactSubComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubCommentServiceTest {


    private SubCommentService underTest;

    @Mock
    SubCommentRepo repo;
    @Mock
    CommentService commentService;

    String commentId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID userUid = UUID.randomUUID();
    Comment comment = new Comment();

    String subCommentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    SubComment subComment = new SubComment();

    @BeforeEach
    void setUp() {
        underTest = new SubCommentService(repo, commentService);
        comment.setId(UUID.fromString(commentId));
        comment.setUserId(userUid);
        comment.setContent("comment test");

        subComment.setId(UUID.fromString(commentId));
        subComment.setContent("first sub comment");
        subComment.setComment(comment);
        subComment.setUserId(userUid);
    }

    @Test
    void should_add_sub_comment_to_comment(){
        given(commentService.getCommentById(commentId)).willReturn(comment);

        underTest.addSubCommentToComment("sub comment test", "paris", UUID.fromString(commentId), userUid);

        ArgumentCaptor<SubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(SubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        SubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getContent(), "sub comment test");
        assertEquals(captured.getLocation(), "paris");
        assertEquals(captured.getUserId(), userUid);
        assertEquals(captured.getComment().getId().toString(), commentId);

    }

    @Test
    void should_ignore_sub_comment(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(subCommentId))).willReturn(Optional.of(subComment));

        underTest.ignoreSubCommentOfUser( subCommentId, userUid);

        ArgumentCaptor<SubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(SubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        SubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getContent(), "first sub comment");
        assertEquals(captured.getUserId(), userUid);
        assertEquals(captured.isIgnored(), true);
        assertEquals(captured.getComment().getId().toString(), commentId);

    }

    @Test
    void should_throw_sub_comment_not_found(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(subCommentId))).willReturn(Optional.empty());

        assertThrows(SubCommentException.SubCommentNotFound.class,
                ()-> underTest.ignoreSubCommentOfUser( subCommentId, userUid),
                "sub comment not found"
        );

    }
    @Test
    void should_throw_sub_comment_not_found_when_subCommentId_is_not_castable_to_UUID(){

        assertThrows(SubCommentException.SubCommentNotFound.class,
                ()-> underTest.ignoreSubCommentOfUser( "1234", userUid),
                "sub comment not found"
        );

    }

    @Test
    void should_throw_not_same_user(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(subCommentId))).willReturn(Optional.of(subComment));

        assertThrows(CommentException.NotSameUser.class,
                ()-> underTest.ignoreSubCommentOfUser( subCommentId, UUID.randomUUID()),
                "user has no authority form this sub comment"
        );

    }

    @Test
    void should_return_sub_comment_with_react_by_post(){
        given(commentService.getCommentById(commentId)).willReturn(comment);
        subComment.setReactComments(Arrays.asList());
        SubComment comment1 = new SubComment();
        comment1.setComment(comment);
        comment1.setContent("comment 2");
        comment1.setUserId(userUid);

        ReactSubComment reactComment = new ReactSubComment();
        reactComment.setReact(ReactEnum.HAHA);
        reactComment.setUserUid(UUID.randomUUID());

        ReactSubComment reactComment2 = new ReactSubComment();
        reactComment2.setReact(ReactEnum.SAD);
        reactComment2.setUserUid(UUID.randomUUID());

        ReactSubComment reactComment3 = new ReactSubComment();
        reactComment3.setReact(ReactEnum.LOVE);
        reactComment3.setUserUid(userUid);

        comment1.setReactComments(Arrays.asList(reactComment, reactComment2, reactComment3));

        given(repo.findByCommentAndIsIgnoredFalse(comment, PageRequest.of(0, 10)))
                .willReturn(Arrays.asList(subComment, comment1));
        given(repo.countByCommentAndIsIgnoredFalse(comment)).willReturn(5);

        WrapperSubCommentResDto wrapper = underTest.getSubCommentsByCommentId(commentId, 0, 10, userUid);

        assertEquals(wrapper.getCount(), 5);
        assertEquals(wrapper.getSubCommentResDtos().get(0).getContent(), "first sub comment");
        assertEquals(wrapper.getSubCommentResDtos().get(1).getContent(), "comment 2");
        assertEquals(wrapper.getSubCommentResDtos().get(1).getReacts().get(ReactEnum.HAHA), 1);
        assertEquals(wrapper.getSubCommentResDtos().get(1).getClientReact(), ReactEnum.LOVE);

    }

    @Test
    void should_throw_bad_request(){
        assertThrows(
                ResponseStatusException.class,
                ()-> underTest.getSubCommentsByCommentId(commentId, -1,10,userUid),
                "page or size cannot be less than zero"
        );
    }
}