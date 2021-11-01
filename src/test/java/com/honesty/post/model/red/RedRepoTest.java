package com.honesty.post.model.red;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.report.Report;
import com.honesty.post.model.report.ReportRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RedRepoTest {

    @Autowired
    RedRepo underTest;
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
        boolean b = underTest.existsRedByPostIdAndUserUid(postUid, userUid);
        assertEquals(b,false);

        Post save = postRepo.save(post);
        Red red = new Red();
        red.setUserUid(userUid);
        red.setPost(save);
        underTest.save(red);
        boolean test = underTest.existsRedByPostIdAndUserUid(save.getId(), userUid);
        assertEquals(test,true);

    }

}