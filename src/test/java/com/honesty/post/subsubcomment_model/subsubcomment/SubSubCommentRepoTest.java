package com.honesty.post.subsubcomment_model.subsubcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubSubCommentRepoTest {


    @Autowired
    SubSubCommentRepo underTest;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    SubCommentRepo subCommentRepo;

    private UUID userUid;
    private Comment comment;
    private Post post;
    private SubComment subComment;

    @BeforeEach
    void setup(){
        post = new Post();
        Post save = postRepo.save(post);
        comment = new Comment();
        comment.setContent("comment content");
        comment.setPost(save);
        Comment save1 = commentRepo.save(comment);

        subComment = new SubComment();
        subComment.setComment(save1);
        subComment.setContent("sub comment content");
    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }


    @Test
    void findByIdAndUserId() {
        SubComment save1 = subCommentRepo.save(subComment);
        SubSubComment comment2 = new SubSubComment();
        comment2.setContent("sub comment test 2");
        comment2.setUserId(userUid);
        comment2.setSubComment(save1);

        SubSubComment comment = new SubSubComment();
        comment.setContent("sub comment test");
        comment.setUserId(userUid);
        comment.setSubComment(save1);

        SubSubComment save = underTest.save(comment);
        SubSubComment save2 = underTest.save(comment2);

        Optional<SubSubComment> byId = underTest.findByIdAndUserId(save.getId(), userUid);

        assertEquals(byId.get().getContent(), "sub comment test");
    }

    @Test
    void findByComment() {
        SubComment comment2 = new SubComment();
        comment2.setComment(comment);
        SubComment commentSaved1 = subCommentRepo.save(comment2);
        SubComment commentSaved2 = subCommentRepo.save(subComment);

        SubSubComment comment = new SubSubComment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setSubComment(commentSaved1);

        SubSubComment comment4 = new SubSubComment();
        comment4.setContent("comment test 2");
        comment4.setUserId(userUid);
        comment4.setSubComment(commentSaved2);



        SubSubComment comment3 = new SubSubComment();
        comment3.setContent("comment test 3");
        comment3.setUserId(userUid);
        comment3.setSubComment(commentSaved1);

        underTest.save(comment);
        underTest.save(comment4);
        underTest.save(comment3);

        List<SubSubComment> byComment = underTest.findBySubCommentAndIsIgnoredFalse(commentSaved1, PageRequest.of(0, 3));

        assertEquals(byComment.get(0).getContent(), "comment test");
        assertEquals(byComment.get(1).getContent(), "comment test 3");
        assertEquals(byComment.size(), 2);
    }

    @Test
    void countByComment() {
        SubComment comment4 = new SubComment();
        comment4.setComment(comment);
        SubComment commentSaved1 = subCommentRepo.save(comment4);
        SubComment commentSaved2 = subCommentRepo.save(subComment);

        SubSubComment comment = new SubSubComment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setSubComment(commentSaved1);

        SubSubComment comment2 = new SubSubComment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setSubComment(commentSaved2);



        SubSubComment comment3 = new SubSubComment();
        comment3.setContent("comment test 3");
        comment3.setUserId(userUid);
        comment3.setSubComment(commentSaved1);

        underTest.save(comment);
        underTest.save(comment2);
        underTest.save(comment3);

        Integer integer = underTest.countBySubCommentAndIsIgnoredFalse(commentSaved1);
        assertEquals(integer, 2);

    }

    @Test
    void findByIdAndIsIgnoredFalse() {

        SubComment commentSaved = subCommentRepo.save(subComment);

        SubSubComment comment = new SubSubComment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setIgnored(true);
        comment.setSubComment(commentSaved);

        SubSubComment comment2 = new SubSubComment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setSubComment(commentSaved);

        SubSubComment save = underTest.save(comment);
        SubSubComment save2 =underTest.save(comment2);

        Optional<SubSubComment> byId = underTest.findByIdAndIsIgnoredFalse(save.getId());
        Optional<SubSubComment> byId2 = underTest.findByIdAndIsIgnoredFalse(save2.getId());

        assertTrue(byId.isEmpty());
        assertTrue(byId2.isPresent());
        assertEquals(byId2.get().getContent(), "comment test 2");
    }
}