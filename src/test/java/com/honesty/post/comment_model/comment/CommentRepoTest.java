package com.honesty.post.comment_model.comment;

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
class CommentRepoTest {

    @Autowired CommentRepo underTest;
    @Autowired
    PostRepo postRepo;

    private UUID userUid;
    private Post post;

    @BeforeEach
    void setup(){
        post = new Post();
        post.setContent("post content");
    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }


    @Test
    void findByIdAndUserId() {
        Post save1 = postRepo.save(post);
        Comment comment2 = new Comment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setPost(save1);

        Comment comment = new Comment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setPost(save1);

        Comment save = underTest.save(comment);
        Comment save2 = underTest.save(comment2);

        Optional<Comment> byId = underTest.findByIdAndUserId(save.getId(), userUid);

        assertEquals(byId.get().getContent(), "comment test");
    }

    @Test
    void findByPost() {
        Post post2 = new Post();
        Post postSaved1 = postRepo.save(post2);
        Post postSaved2 = postRepo.save(post);

        Comment comment = new Comment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setPost(postSaved1);

        Comment comment2 = new Comment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setPost(postSaved2);



        Comment comment3 = new Comment();
        comment3.setContent("comment test 3");
        comment3.setUserId(userUid);
        comment3.setPost(postSaved1);

        underTest.save(comment);
        underTest.save(comment2);
        underTest.save(comment3);

        List<Comment> byPost = underTest.findByPost(postSaved1, PageRequest.of(0, 3));

        assertEquals(byPost.get(0).getContent(), "comment test");
        assertEquals(byPost.get(1).getContent(), "comment test 3");
        assertEquals(byPost.size(), 2);
    }

    @Test
    void countByPost() {
        Post post2 = new Post();
        Post postSaved1 = postRepo.save(post2);
        Post postSaved2 = postRepo.save(post);

        Comment comment = new Comment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setPost(postSaved1);

        Comment comment2 = new Comment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setPost(postSaved2);



        Comment comment3 = new Comment();
        comment3.setContent("comment test 3");
        comment3.setUserId(userUid);
        comment3.setPost(postSaved1);

        underTest.save(comment);
        underTest.save(comment2);
        underTest.save(comment3);

        Integer integer = underTest.countByPostAndIsIgnoredFalse(postSaved1);
        assertEquals(integer, 2);

    }

    @Test
    void findByIdAndIsIgnoredFalse() {

        Post postSaved = postRepo.save(post);

        Comment comment = new Comment();
        comment.setContent("comment test");
        comment.setUserId(userUid);
        comment.setIgnored(true);
        comment.setPost(postSaved);

        Comment comment2 = new Comment();
        comment2.setContent("comment test 2");
        comment2.setUserId(userUid);
        comment2.setPost(postSaved);

        Comment save = underTest.save(comment);
        Comment save2 =underTest.save(comment2);

        Optional<Comment> byId = underTest.findByIdAndIsIgnoredFalse(save.getId());
        Optional<Comment> byId2 = underTest.findByIdAndIsIgnoredFalse(save2.getId());

        assertTrue(byId.isEmpty());
        assertTrue(byId2.isPresent());
        assertEquals(byId2.get().getContent(), "comment test 2");
    }
}