package com.honesty.post.subcomment_model.subcomment;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
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
class SubCommentRepoTest {


    @Autowired
    SubCommentRepo underTest;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    PostRepo postRepo;

    private UUID userUid;
    private Comment comment;
    private Post post;

    @BeforeEach
    void setup(){
        post = new Post();
        Post save = postRepo.save(post);
        comment = new Comment();
        comment.setContent("comment content");
        comment.setPost(save);
    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }


    @Test
    void findByIdAndUserId() {
        Comment save1 = commentRepo.save(comment);
        SubComment comment2 = new SubComment();
        comment2.setContent("sub comment test 2");
        comment2.setUserId(userUid);
        comment2.setComment(save1);

        SubComment comment = new SubComment();
        comment.setContent("sub comment test");
        comment.setUserId(userUid);
        comment.setComment(save1);

        SubComment save = underTest.save(comment);
        SubComment save2 = underTest.save(comment2);

        Optional<SubComment> byId = underTest.findByIdAndUserId(save.getId(), userUid);

        assertEquals(byId.get().getContent(), "sub comment test");
    }

    @Test
    void findByComment() {
        Comment comment2 = new Comment();
        comment2.setPost(post);
        Comment commentSaved1 = commentRepo.save(comment2);
        Comment commentSaved2 = commentRepo.save(comment);

        SubComment comment = new SubComment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setComment(commentSaved1);

        SubComment comment4 = new SubComment();
        comment4.setContent("comment test 2");
        comment4.setUserId(userUid);
        comment4.setComment(commentSaved2);



        SubComment comment3 = new SubComment();
        comment3.setContent("comment test 3");
        comment3.setUserId(userUid);
        comment3.setComment(commentSaved1);

        underTest.save(comment);
        underTest.save(comment4);
        underTest.save(comment3);

        List<SubComment> byComment = underTest.findByCommentAndIsIgnoredFalse(commentSaved1, PageRequest.of(0, 3));

        assertEquals(byComment.get(0).getContent(), "comment test");
        assertEquals(byComment.get(1).getContent(), "comment test 3");
        assertEquals(byComment.size(), 2);
    }

    @Test
    void countByComment() {
        Comment comment4 = new Comment();
        comment4.setPost(post);
        Comment commentSaved1 = commentRepo.save(comment4);
        Comment commentSaved2 = commentRepo.save(comment);

        SubComment comment = new SubComment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setComment(commentSaved1);

        SubComment comment2 = new SubComment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setComment(commentSaved2);



        SubComment comment3 = new SubComment();
        comment3.setContent("comment test 3");
        comment3.setUserId(userUid);
        comment3.setComment(commentSaved1);

        underTest.save(comment);
        underTest.save(comment2);
        underTest.save(comment3);

        Integer integer = underTest.countByCommentAndIsIgnoredFalse(commentSaved1);
        assertEquals(integer, 2);

    }

    @Test
    void findByIdAndIsIgnoredFalse() {

        Comment commentSaved = commentRepo.save(comment);

        SubComment comment = new SubComment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setIgnored(true);
        comment.setComment(commentSaved);

        SubComment comment2 = new SubComment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setComment(commentSaved);

        SubComment save = underTest.save(comment);
        SubComment save2 =underTest.save(comment2);

        Optional<SubComment> byId = underTest.findByIdAndIsIgnoredFalse(save.getId());
        Optional<SubComment> byId2 = underTest.findByIdAndIsIgnoredFalse(save2.getId());

        assertTrue(byId.isEmpty());
        assertTrue(byId2.isPresent());
        assertEquals(byId2.get().getContent(), "comment test 2");
    }
}