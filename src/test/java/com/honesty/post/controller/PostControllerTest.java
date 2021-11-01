package com.honesty.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honesty.post.model.post.CustomPost;
import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.security.JwtConfig;
import io.jsonwebtoken.security.Keys;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@RunWith(SpringRunner.class)
class PostControllerTest {

    @Autowired ObjectMapper mapper;
    @Autowired private MockMvc mvc;

    private String bearerToken;
    @MockBean
    PostService postService;
    @MockBean private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        given(jwtConfig.getTokenPrefix()).willReturn("Bearer ");
        given(jwtConfig.getSecretKey()).willReturn(Keys.hmacShaKeyFor("chqbqfdf84dsfq54fsdf6q54sdf6q5s4dc15".getBytes()));
        bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Yjc2NTE2OC0yNmU4LTQxODAtYmZhZC03N2I3NDdjMzU1MjQiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9NRU1CRVIifSx7ImF1dGhvcml0eSI6ImNvbW1lbnQ6d3JpdGUifSx7ImF1dGhvcml0eSI6InBvc3Q6d3JpdGUifSx7ImF1dGhvcml0eSI6Im1lc3NhZ2U6d3JpdGUifV0sImlhdCI6MTYzMDg0MDk0MiwiZXhwIjoxNzUxNzU2NDAwfQ.kTeMd7XygK7PXcK5XVwuUmg1pBD1dGilNKobjW5jObg";
    }

    @Test
    void it_should_add_new_post()throws Exception{

        Map<String, String> map = Map.of(
                "content", "test content",
                "location","tunis"
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/post/add")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(map));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        verify(postService).addNewPost("test content","tunis", UUID.fromString("5b765168-26e8-4180-bfad-77b747c35524"));

    }

    @Test
    void should_throw_exception_when_content_is_empty_or_null()throws Exception{
        Map<String, String> map = Map.of(
                "location","tunis"
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/post/add")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(map));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void should_ignore_post_when_the_params_are_correct() throws Exception{

        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/post/ignore/123")
                .header("Authorization", bearerToken);
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        verify(postService).ignorePost(
                "123", UUID.fromString("5b765168-26e8-4180-bfad-77b747c35524"));

    }

    @Test
    void should_get_the_definition_of_post() throws Exception{

        Map<String, Object> map = Map.of(
                "id","aaa",
                "commentCount",5,
                "mostCommentReacted","most reacted",
                "mostCommentReactedUser","bbbb",
                "hahaCount",0,
                "angryCount",10,
                "sadCount",12,
                "loveCount",0,
                "burkCount",3

        );

        CustomPost customPost = new CustomPost() {
            @Override
            public String getId() {
                return "aaa";
            }

            @Override
            public String getContent() {
                return "content test";
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return LocalDateTime.of(2000, Month.APRIL,10,12,0);
            }

            @Override
            public String getUserId() {
                return "user id";
            }

            @Override
            public Integer getCommentCount() {
                return 5;
            }

            @Override
            public String getMostCommentReacted() {
                return "most reacted";
            }

            @Override
            public String getMostCommentReactedUser() {
                return "bbbb";
            }

            @Override
            public Integer getHahaCount() {
                return 0;
            }

            @Override
            public Integer getAngryCount() {
                return 10;
            }

            @Override
            public Integer getSadCount() {
                return 12;
            }

            @Override
            public Integer getLoveCount() {
                return 0;
            }

            @Override
            public Integer getBurkCount() {
                return 3;
            }
        };
        given(postService.getCustomPostById("123")).willReturn(customPost);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/post-api/post/definition/123")
                .header("Authorization", bearerToken);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(map)))
                .andReturn();
    }

    @Test
    void should_get_posts_by_location()throws Exception{
        Post post = new Post();
        post.setContent("first");
        Post post2 = new Post();
        post2.setContent("second");

        List<Post> posts = Arrays.asList(post, post2);

        given(postService.getPostsByLocation("tunis")).willReturn(posts);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/post-api/post")
                .param("location", "tunis")
                .header("Authorization", bearerToken);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":null,\"content\":\"first\",\"userId\":null,\"createdAt\":null,\"location\":null},{\"id\":null,\"content\":\"second\",\"userId\":null,\"createdAt\":null,\"location\":null}]"))
                .andReturn();
    }
}