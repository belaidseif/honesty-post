package com.honesty.post.comment_model.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.model.post.Post;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Comment {

    public Comment(){
        createdAt = ZonedDateTime.now();
        isIgnored = false;

    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String content;
    private String location;
    private ZonedDateTime createdAt;
    private UUID userId;

    private boolean isIgnored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "post_id"
    )
    @JsonIgnore
    private Post post;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "comment")
    private List<ReactComment> reactComments;
}
