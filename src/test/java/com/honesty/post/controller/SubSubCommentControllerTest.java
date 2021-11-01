package com.honesty.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honesty.post.controller.dto.res.SubCommentResDto;
import com.honesty.post.controller.dto.res.SubSubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubSubCommentResDto;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.security.JwtConfig;
import com.honesty.post.service.UserService;
import com.honesty.post.subcomment_model.react.ReactSubCommentService;
import com.honesty.post.subcomment_model.report.ReportSubCommentService;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import com.honesty.post.subsubcomment_model.react.ReactSubSubCommentService;
import com.honesty.post.subsubcomment_model.report.ReportSubSubCommentService;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubSubCommentController.class)
@RunWith(SpringRunner.class)
class SubSubCommentControllerTest {


    @Autowired
    ObjectMapper mapper;
    @Autowired private MockMvc mvc;

    private String bearerToken;

    @MockBean
    SubSubCommentService subSubCommentService;
    @MockBean
    UserService userService;
    @MockBean
    ReportSubSubCommentService reportSubCommentService;
    @MockBean
    ReactSubSubCommentService reactSubCommentService;



    @MockBean private JwtConfig jwtConfig;

    private UUID subCommentUid = UUID.randomUUID();
    private UUID userUid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        given(jwtConfig.getTokenPrefix()).willReturn("Bearer ");
        given(userService.getUserUid(any())).willReturn(userUid);
        given(jwtConfig.getSecretKey()).willReturn(Keys.hmacShaKeyFor("chqbqfdf84dsfq54fsdf6q54sdf6q5s4dc15".getBytes()));
        bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Yjc2NTE2OC0yNmU4LTQxODAtYmZhZC03N2I3NDdjMzU1MjQiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9NRU1CRVIifSx7ImF1dGhvcml0eSI6ImNvbW1lbnQ6d3JpdGUifSx7ImF1dGhvcml0eSI6InBvc3Q6d3JpdGUifSx7ImF1dGhvcml0eSI6Im1lc3NhZ2U6d3JpdGUifV0sImlhdCI6MTYzMDg0MDk0MiwiZXhwIjoxNzUxNzU2NDAwfQ.kTeMd7XygK7PXcK5XVwuUmg1pBD1dGilNKobjW5jObg";
    }

    @Test
    void should_add_sub_comment()throws Exception{
        Map<String, String> map = Map.of(
                "content", "test sub content",
                "location","tunis",
                "subCommentUid", subCommentUid.toString()
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/sub-sub-comment")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(map));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        verify(subSubCommentService).addSubSubCommentToComment(
                "test sub content",
                "tunis",
                subCommentUid,
                userUid);

    }
    @Test
    void should_return_bad_request_when_commentUid_is_null()throws Exception{
        Map<String, String> map = Map.of(
                "content", "test sub content",
                "location","tunis"
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/sub-sub-comment")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(map));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(subSubCommentService , never()).addSubSubCommentToComment(
                any(),
                any(),
                any(),
                any());

    }

    @Test
    void should_ignore_sub_comment()throws Exception{


        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/sub-sub-comment/ignore/123")
                .header("Authorization", bearerToken);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        verify(subSubCommentService).ignoreSubSubCommentOfUser(
                "123",
                userUid);

    }

    @Test
    void should_report_sub_comment()throws Exception{


        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/sub-sub-comment/report/123")
                .header("Authorization", bearerToken);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        verify(reportSubCommentService).reportSubSubCommentOfUser(
                "123",
                userUid);

    }

    @Test
    void should_react_to_sub_comment()throws Exception{


        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/sub-sub-comment/react/123")
                .header("Authorization", bearerToken)
                .param("react", "LOVE");

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        verify(reactSubCommentService).addOrRemoveReact(
                "123",
                ReactEnum.LOVE,
                userUid);
    }

    @Test
    void should_return_bad_request_when_react_is_incorrect()throws Exception{


        RequestBuilder request = MockMvcRequestBuilders
                .post("/post-api/sub-sub-comment/react/123")
                .header("Authorization", bearerToken)
                .param("react", "AAA");

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(reactSubCommentService, never()).addOrRemoveReact(
                any(),
                any(),
                any());
    }

    @Test
    void should_return_sub_comments()throws Exception{
        MultiValueMap<String,String> map =new LinkedMultiValueMap<>();
        map.add("page", "0");
        map.add("size", "10");

        SubSubCommentResDto res = new SubSubCommentResDto(
                UUID.randomUUID(), "content comment" ,null, null, userUid
        );

        WrapperSubSubCommentResDto wrapper = new WrapperSubSubCommentResDto(Arrays.asList(res), 5);

        given(subSubCommentService.getSubSubCommentsBySubCommentId(
                "1234", 0,10, userUid)).willReturn(wrapper);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/post-api/sub-sub-comment/by-sub-comment/1234")
                .header("Authorization", bearerToken)
                .params(map);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(wrapper)))
                .andReturn();

    }

    @Test
    void should_return_bad_request_when_size_is_missed()throws Exception{

        RequestBuilder request = MockMvcRequestBuilders
                .get("/post-api/sub-sub-comment/by-sub-comment/1234")
                .header("Authorization", bearerToken)
                .param("page", "5");

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }
}