package com.honesty.post.subsubcomment_model.subsubcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentService;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubSubCommentResDto;
import com.honesty.post.exception.CommentException;
import com.honesty.post.exception.SubCommentException;
import com.honesty.post.exception.SubSubCommentException;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.react.ReactSubComment;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentRepo;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import com.honesty.post.subsubcomment_model.react.ReactSubSubComment;
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
class SubSubCommentServiceTest {

    private SubSubCommentService underTest;

    @Mock
    SubSubCommentRepo repo;
    @Mock
    SubCommentService commentService;

    String commentId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID userUid = UUID.randomUUID();
    SubComment comment = new SubComment();

    String subCommentId = "c0da96f8-350c-11ec-8d3d-0242ac130003";
    SubSubComment subComment = new SubSubComment();

    @BeforeEach
    void setUp() {
        underTest = new SubSubCommentService( commentService,repo);
        comment.setId(UUID.fromString(commentId));
        comment.setUserId(userUid);
        comment.setContent("comment test");

        subComment.setId(UUID.fromString(commentId));
        subComment.setContent("first sub comment");
        subComment.setSubComment(comment);
        subComment.setUserId(userUid);
    }

    @Test
    void should_add_sub_comment_to_comment(){
        given(commentService.getSubCommentById(commentId)).willReturn(comment);

        underTest.addSubSubCommentToComment("sub comment test", "paris", UUID.fromString(commentId), userUid);

        ArgumentCaptor<SubSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(SubSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        SubSubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getContent(), "sub comment test");
        assertEquals(captured.getLocation(), "paris");
        assertEquals(captured.getUserId(), userUid);
        assertEquals(captured.getSubComment().getId().toString(), commentId);

    }

    @Test
    void should_ignore_sub_comment(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(subCommentId))).willReturn(Optional.of(subComment));

        underTest.ignoreSubSubCommentOfUser( subCommentId, userUid);

        ArgumentCaptor<SubSubComment> userEntityArgumentCaptor = ArgumentCaptor.forClass(SubSubComment.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        SubSubComment captured = userEntityArgumentCaptor.getValue();

        assertEquals(captured.getContent(), "first sub comment");
        assertEquals(captured.getUserId(), userUid);
        assertEquals(captured.isIgnored(), true);
        assertEquals(captured.getSubComment().getId().toString(), commentId);

    }

    @Test
    void should_throw_sub_comment_not_found(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(subCommentId))).willReturn(Optional.empty());

        assertThrows(SubSubCommentException.SubSubCommentNotFound.class,
                ()-> underTest.ignoreSubSubCommentOfUser( subCommentId, userUid),
                "sub sub comment not found"
        );

    }
    @Test
    void should_throw_sub_comment_not_found_when_subCommentId_is_not_castable_to_UUID(){

        assertThrows(SubSubCommentException.SubSubCommentNotFound.class,
                ()-> underTest.ignoreSubSubCommentOfUser( "1234", userUid),
                "sub sub comment not found"
        );

    }

    @Test
    void should_throw_not_same_user(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(subCommentId))).willReturn(Optional.of(subComment));

        assertThrows(CommentException.NotSameUser.class,
                ()-> underTest.ignoreSubSubCommentOfUser( subCommentId, UUID.randomUUID()),
                "user has no authority form this sub comment"
        );

    }

    @Test
    void should_return_sub_comment_with_react_by_post(){
        given(commentService.getSubCommentById(commentId)).willReturn(comment);
        subComment.setReactComments(Arrays.asList());
        SubSubComment comment1 = new SubSubComment();
        comment1.setSubComment(comment);
        comment1.setContent("comment 2");
        comment1.setUserId(userUid);

        ReactSubSubComment reactComment = new ReactSubSubComment();
        reactComment.setReact(ReactEnum.HAHA);
        reactComment.setUserUid(UUID.randomUUID());

        ReactSubSubComment reactComment2 = new ReactSubSubComment();
        reactComment2.setReact(ReactEnum.SAD);
        reactComment2.setUserUid(UUID.randomUUID());

        ReactSubSubComment reactComment3 = new ReactSubSubComment();
        reactComment3.setReact(ReactEnum.LOVE);
        reactComment3.setUserUid(userUid);

        comment1.setReactComments(Arrays.asList(reactComment, reactComment2, reactComment3));

        given(repo.findBySubCommentAndIsIgnoredFalse(comment, PageRequest.of(0, 10)))
                .willReturn(Arrays.asList(subComment, comment1));
        given(repo.countBySubCommentAndIsIgnoredFalse(comment)).willReturn(5);

        WrapperSubSubCommentResDto wrapper = underTest.getSubSubCommentsBySubCommentId(commentId, 0, 10, userUid);

        assertEquals(wrapper.getCount(), 5);
        assertEquals(wrapper.getSubSubCommentResDtos().get(0).getContent(), "first sub comment");
        assertEquals(wrapper.getSubSubCommentResDtos().get(1).getContent(), "comment 2");
        assertEquals(wrapper.getSubSubCommentResDtos().get(1).getReacts().get(ReactEnum.HAHA), 1);
        assertEquals(wrapper.getSubSubCommentResDtos().get(1).getClientReact(), ReactEnum.LOVE);

    }

    @Test
    void should_throw_bad_request(){
        assertThrows(
                ResponseStatusException.class,
                ()-> underTest.getSubSubCommentsBySubCommentId(commentId, -1,10,userUid),
                "page or size cannot be less than zero"
        );
    }

}