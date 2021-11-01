package com.honesty.post.subcomment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.comment_model.report.ReportComment;
import com.honesty.post.comment_model.report.ReportCommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReportSubCommentRepoTest {


    @Autowired
    ReportSubCommentRepo underTest;
    @Autowired
    SubCommentRepo subCommentRepo;
    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentRepo commentRepo;

    private UUID userUid;
    private Post post;
    private Comment comment;
    private SubComment subComment;

    @BeforeEach
    void setup(){
        post = new Post();
        Post save = postRepo.save(post);

        comment = new Comment();
        comment.setContent("post content");
        comment.setPost(save);

        subComment = new SubComment();
        subComment.setContent("sub comment content");

    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByCommentAndUserUid() {
        Comment save = commentRepo.save(comment);
        subComment.setComment(save);
        SubComment subCommentSaved = subCommentRepo.save(subComment);

        ReportSubComment report =new ReportSubComment();
        report.setSubComment(subCommentSaved);
        report.setUserUid(userUid);

        underTest.save(report);

        boolean b = underTest.existsBySubCommentAndUserUid(subCommentSaved, UUID.randomUUID());
        boolean c = underTest.existsBySubCommentAndUserUid(subCommentSaved, userUid);

        assertFalse(b);
        assertTrue(c);
    }
}