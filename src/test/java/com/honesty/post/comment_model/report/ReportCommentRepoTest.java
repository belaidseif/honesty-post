package com.honesty.post.comment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.comment_model.react.ReactCommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReportCommentRepoTest {

    @Autowired
    ReportCommentRepo underTest;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    PostRepo postRepo;

    private UUID userUid;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setup(){
        post = new Post();
        comment = new Comment();
        comment.setContent("post content");

    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByCommentAndUserUid() {
        Post save = postRepo.save(post);
        comment.setPost(save);
        Comment commentSaved = commentRepo.save(comment);

        ReportComment report =new ReportComment();
        report.setComment(commentSaved);
        report.setUserUid(userUid);

        underTest.save(report);

        boolean b = underTest.existsByCommentAndUserUid(commentSaved, UUID.randomUUID());
        boolean c = underTest.existsByCommentAndUserUid(commentSaved, userUid);

        assertFalse(b);
        assertTrue(c);
    }
}