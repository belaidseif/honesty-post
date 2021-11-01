package com.honesty.post.model.seen;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SeenRepoTest {

    @Autowired
    SeenRepo underTest;
    @Autowired
    PostRepo postRepo;

    private UUID postUid =UUID.randomUUID();
    private UUID userUid =UUID.randomUUID();
    private Post post = new Post();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsReportByPostIdAndUserUid(){
        boolean b = underTest.existsSeenByPostIdAndUserUid(postUid, userUid);
        assertEquals(b,false);

        Post save = postRepo.save(post);
        Seen seen = new Seen();
        seen.setUserUid(userUid);
        seen.setPost(save);
        underTest.save(seen);
        boolean test = underTest.existsSeenByPostIdAndUserUid(save.getId(), userUid);
        assertEquals(test,true);

    }

}