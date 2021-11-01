package com.honesty.post.model.post;

import com.honesty.post.exception.CommentException;
import com.honesty.post.exception.PostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private PostService underTest;

    @Mock PostRepo repo;


    String postId = "99a0a470-3402-11ec-8d3d-0242ac130003";
    UUID uuid = UUID.randomUUID();
    Post post = new Post();

    @BeforeEach
    void setUp() {
        underTest = new PostService(repo);
        post.setId(UUID.fromString(postId));
        post.setUserId(uuid);
        post.setContent("post test");
        post.setIgnored(false);
    }

    @Test
    void it_should_add_new_post(){
        underTest.addNewPost("first content", "tunis", uuid);

        ArgumentCaptor<Post> userEntityArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        Post capturedUserEntity = userEntityArgumentCaptor.getValue();

        assertThat(capturedUserEntity.getUserId()).isEqualTo(uuid);
        assertThat(capturedUserEntity.getContent()).isEqualTo("first content");
        assertThat(capturedUserEntity.getLocation()).isEqualTo("tunis");
        assertThat(capturedUserEntity.isIgnored()).isEqualTo(false);
    }

    @Test
    void should_set_is_ignored_to_true_when_ignore_method_is_called_with_correct_params(){

        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(postId))).willReturn(Optional.of(post));
        underTest.ignorePost(postId, uuid);

        ArgumentCaptor<Post> userEntityArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(repo).save(userEntityArgumentCaptor.capture());
        Post capturedUserEntity = userEntityArgumentCaptor.getValue();

        assertThat(capturedUserEntity.getContent()).isEqualTo("post test");
        assertThat(capturedUserEntity.isIgnored()).isEqualTo(true);

    }

    @Test
    void should_throw_exception_when_the_user_has_no_authorities(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(postId))).willReturn(Optional.of(post));
        assertThatThrownBy(()-> underTest.ignorePost(postId, UUID.randomUUID()))
                .isInstanceOf(CommentException.NotSameUser.class)
                .hasMessageContaining("user has no authorities");
        verify(repo, never()).save(any());

    }

    @Test
    void should_throw_exception_when_post_does_not_exist(){
        given(repo.findByIdAndIsIgnoredFalse(UUID.fromString(postId))).willReturn(Optional.empty());

        assertThatThrownBy(()-> underTest.ignorePost(postId, uuid))
                .isInstanceOf(PostException.PostNotFound.class)
                .hasMessageContaining("post not found");
        verify(repo, never()).save(any());
    }

    @Test
    void should_get_custom_post_by_id(){
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
        given(repo.findCustomPostById(UUID.fromString(postId))).willReturn(Optional.of(customPost));
        CustomPost customPost1 = underTest.getCustomPostById(postId);

        assertThat(customPost1.getAngryCount()).isEqualTo(10);
        assertThat(customPost1.getBurkCount()).isEqualTo(3);
        assertThat(customPost1.getMostCommentReacted()).isEqualTo("most reacted");
        assertThat(customPost1.getContent()).isEqualTo("content test");
        assertThat(customPost1.getId()).isEqualTo("aaa");
        assertThat(customPost1.getMostCommentReactedUser()
        ).isEqualTo("bbbb");
        assertThat(customPost1.getUserId()).isEqualTo("user id");

    }

}