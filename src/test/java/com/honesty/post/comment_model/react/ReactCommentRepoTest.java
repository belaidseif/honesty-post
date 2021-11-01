package com.honesty.post.comment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.react.ReactEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReactCommentRepoTest {

    @Autowired
    ReactCommentRepo underTest;
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
    void findByCommentAndUserUid(){
        Post save1 = postRepo.save(post);
        comment.setPost(save1);
        Comment save = commentRepo.save(comment);

        ReactComment reactComment = new ReactComment();
        reactComment.setReact(ReactEnum.ANGRY);
        reactComment.setComment(save);
        reactComment.setUserUid(userUid);

        ReactComment reactComment2 = new ReactComment();
        reactComment2.setReact(ReactEnum.SAD);
        reactComment2.setComment(save);
        reactComment2.setUserUid(UUID.randomUUID());

        underTest.save(reactComment);
        underTest.save(reactComment2);

        Optional<ReactComment> byComment = underTest.findByCommentAndUserUid(save, userUid);
        assertEquals(byComment.get().getReact(), ReactEnum.ANGRY);
    }

}