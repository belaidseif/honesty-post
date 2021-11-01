package com.honesty.post.subsubcomment_model.report;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.subcomment_model.report.ReportSubComment;
import com.honesty.post.subcomment_model.report.ReportSubCommentRepo;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentRepo;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReportSubSubCommentRepoTest {


    @Autowired
    ReportSubSubCommentRepo underTest;
    @Autowired
    SubCommentRepo subCommentRepo;
    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    SubSubCommentRepo subSubCommentRepo;

    private UUID userUid;
    private Post post;
    private Comment comment;
    private SubComment subComment;
    private SubSubComment subSubComment;

    @BeforeEach
    void setup(){
        post = new Post();
        Post save = postRepo.save(post);

        comment = new Comment();
        comment.setContent("post content");
        comment.setPost(save);
        Comment save1 = commentRepo.save(comment);

        subComment = new SubComment();
        subComment.setContent("sub comment content");
        subComment.setComment(save1);

        subSubComment = new SubSubComment();
        subSubComment.setContent("sub sub comment content");

    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByCommentAndUserUid() {
        SubComment save = subCommentRepo.save(subComment);
        subSubComment.setSubComment(save);
        SubSubComment subCommentSaved = subSubCommentRepo.save(subSubComment);

        ReportSubSubComment report =new ReportSubSubComment();
        report.setSubSubComment(subCommentSaved);
        report.setUserUid(userUid);

        underTest.save(report);

        boolean b = underTest.existsBySubSubCommentAndUserUid(subCommentSaved, UUID.randomUUID());
        boolean c = underTest.existsBySubSubCommentAndUserUid(subCommentSaved, userUid);

        assertFalse(b);
        assertTrue(c);
    }
}