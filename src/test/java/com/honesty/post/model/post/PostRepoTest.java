package com.honesty.post.model.post;

import com.honesty.post.model.react.React;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.model.react.ReactRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepoTest {

    @Autowired
    PostRepo underTest;
    @Autowired
    ReactRepo reactRepo;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByIdAndIsIgnoredFalse(){

        Post post = new Post();
        Post save = underTest.save(post);
        Optional<Post> byId = underTest.findByIdAndIsIgnoredFalse(save.getId());

        Post post2 = new Post();
        post2.setIgnored(true);
        Post save2 = underTest.save(post2);
        Optional<Post> byId2 = underTest.findByIdAndIsIgnoredFalse(save2.getId());

        assertTrue(byId.isPresent());
        assertTrue(byId2.isEmpty());
    }

    @Test
    void findCustomPostById(){
        Post post = new Post();
        post.setContent("content test");
        Post save = underTest.save(post);

        React react = new React();
        react.setReact(ReactEnum.ANGRY);
        react.setPost(save);
        reactRepo.save(react);

        React react2 = new React();
        react2.setReact(ReactEnum.SAD);
        react2.setPost(save);
        reactRepo.save(react2);

        React react3 = new React();
        react3.setReact(ReactEnum.ANGRY);
        react3.setPost(save);
        reactRepo.save(react3);

        Optional<CustomPost> customPostById = underTest.findCustomPostById(save.getId());

        assertSame(customPostById.get().getContent(), "content test");
        assertSame(customPostById.get().getAngryCount(), 2);
        assertSame(customPostById.get().getSadCount(), 1);
        assertSame(customPostById.get().getCommentCount(), 0);
        assertSame(customPostById.get().getBurkCount(), 0);

    }

    @Test
    void findByLocationAndIsIgnoredFalseLimitedTo(){
        Post post = new Post();
        post.setContent("content test");
        post.setLocation("tunis");
        underTest.save(post);

        Post post2 = new Post();
        post2.setContent("content test 2");
        post2.setLocation("tunis");
        underTest.save(post2);

        Post post3 = new Post();
        post3.setContent("content test 3");
        post3.setLocation("tunis");
        post3.setIgnored(true);
        underTest.save(post3);

        Post post4 = new Post();
        post4.setContent("content test 4");
        post4.setLocation("paris");
        underTest.save(post4);

        Post post5 = new Post();
        post5.setContent("content test 5");
        post5.setLocation("tunis");
        underTest.save(post5);

        List<Post> all = underTest.findByLocationAndIsIgnoredFalseLimitedTo("tunis", 3);

        assertEquals(all.get(0).getContent(), "content test");
        assertEquals(all.get(1).getContent(), "content test 2");
        assertEquals(all.get(2).getContent(), "content test 5");

    }
}