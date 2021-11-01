package com.honesty.post.model.report;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostRepo;
import com.honesty.post.model.react.ReactRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReportRepoTest {

    @Autowired
    ReportRepo underTest;
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
        boolean b = underTest.existsReportByPostIdAndUserUid(postUid, userUid);
        assertEquals(b,false);

        Post save = postRepo.save(post);
        Report report = new Report();
        report.setUserUid(userUid);
        report.setPost(save);
        underTest.save(report);
        boolean test = underTest.existsReportByPostIdAndUserUid(save.getId(), userUid);
        assertEquals(test,true);

    }

}