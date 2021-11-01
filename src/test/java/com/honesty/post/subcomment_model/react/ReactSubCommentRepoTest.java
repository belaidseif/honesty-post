package com.honesty.post.subcomment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.comment_model.react.ReactCommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReactSubCommentRepoTest {


    @Autowired
    ReactSubCommentRepo underTest;
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
    void findByCommentAndUserUid(){
        Comment save1 = commentRepo.save(comment);
        subComment.setComment(save1);
        SubComment save = subCommentRepo.save(subComment);

        ReactSubComment reactComment = new ReactSubComment();
        reactComment.setReact(ReactEnum.ANGRY);
        reactComment.setSubComment(save);
        reactComment.setUserUid(userUid);

        ReactSubComment reactComment2 = new ReactSubComment();
        reactComment2.setReact(ReactEnum.SAD);
        reactComment2.setSubComment(save);
        reactComment2.setUserUid(UUID.randomUUID());

        underTest.save(reactComment);
        underTest.save(reactComment2);

        Optional<ReactSubComment> byComment = underTest.findBySubCommentAndUserUid(save, userUid);
        assertEquals(byComment.get().getReact(), ReactEnum.ANGRY);
    }
}