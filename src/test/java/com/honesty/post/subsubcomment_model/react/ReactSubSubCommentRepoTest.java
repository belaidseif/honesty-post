package com.honesty.post.subsubcomment_model.react;

import com.honesty.post.comment_model.comment.Comment;
import com.honesty.post.comment_model.comment.CommentRepo;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.subcomment_model.react.ReactSubComment;
import com.honesty.post.subcomment_model.react.ReactSubCommentRepo;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subcomment_model.subcomment.SubCommentRepo;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReactSubSubCommentRepoTest {


    @Autowired
    ReactSubSubCommentRepo underTest;
    @Autowired
    SubCommentRepo subCommentRepo;
    @Autowired
    SubSubCommentRepo subSubCommentRepo;
    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentRepo commentRepo;

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
        Comment save2 = commentRepo.save(comment);

        subComment = new SubComment();
        subComment.setContent("sub comment content");
        subComment.setComment(save2);
        SubComment save1 = subCommentRepo.save(subComment);

        subSubComment = new SubSubComment();
        subSubComment.setContent("sub sub comment content");

    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByCommentAndUserUid(){
        SubComment save1 = subCommentRepo.save(subComment);
        subSubComment.setSubComment(save1);
        SubSubComment save = subSubCommentRepo.save(subSubComment);

        ReactSubSubComment reactComment = new ReactSubSubComment();
        reactComment.setReact(ReactEnum.ANGRY);
        reactComment.setSubSubComment(save);
        reactComment.setUserUid(userUid);

        ReactSubSubComment reactComment2 = new ReactSubSubComment();
        reactComment2.setReact(ReactEnum.SAD);
        reactComment2.setSubSubComment(save);
        reactComment2.setUserUid(UUID.randomUUID());

        underTest.save(reactComment);
        underTest.save(reactComment2);

        Optional<ReactSubSubComment> byComment = underTest.findBySubSubCommentAndUserUid(save, userUid);
        assertEquals(byComment.get().getReact(), ReactEnum.ANGRY);
    }
}