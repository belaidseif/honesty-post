package com.honesty.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.model.react.ReactService;
import com.honesty.post.security.JwtConfig;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReactionController.class)
@RunWith(SpringRunner.class)
class ReactionControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private String bearerToken;
    @MockBean
    ReactService reactService;
    @MockBean
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        given(jwtConfig.getTokenPrefix()).willReturn("Bearer ");
        given(jwtConfig.getSecretKey()).willReturn(Keys.hmacShaKeyFor("chqbqfdf84dsfq54fsdf6q54sdf6q5s4dc15".getBytes()));
        bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Yjc2NTE2OC0yNmU4LTQxODAtYmZhZC03N2I3NDdjMzU1MjQiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9NRU1CRVIifSx7ImF1dGhvcml0eSI6ImNvbW1lbnQ6d3JpdGUifSx7ImF1dGhvcml0eSI6InBvc3Q6d3JpdGUifSx7ImF1dGhvcml0eSI6Im1lc3NhZ2U6d3JpdGUifV0sImlhdCI6MTYzMDg0MDk0MiwiZXhwIjoxNzUxNzU2NDAwfQ.kTeMd7XygK7PXcK5XVwuUmg1pBD1dGilNKobjW5jObg";
    }

    @Test
    void should_call_service_with_the_correct_params()throws Exception{
        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/reaction/react/123")
                .header("Authorization", bearerToken)
                .param("react", "HAHA");

        mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        verify(reactService).addOrRemoveReact(
                 ReactEnum.HAHA, "123",UUID.fromString("5b765168-26e8-4180-bfad-77b747c35524"));
    }

    @Test
    void should_return_bad_request_when_the_params_are_incorrect()throws Exception{
        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/reaction/react/123")
                .header("Authorization", bearerToken)
                .param("react", "AAA");

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(reactService, never()).addOrRemoveReact(any(),any(),any());
    }
}