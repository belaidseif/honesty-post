package com.honesty.post.subcomment_model.subcomment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honesty.post.comment_model.comment.Comment;

import com.honesty.post.comment_model.react.ReactComment;
import com.honesty.post.subcomment_model.react.ReactSubComment;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class SubComment {

    public SubComment(){
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
            name = "comment_id"
    )
    @JsonIgnore
    private Comment comment;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subComment")
    private List<ReactSubComment> reactComments;
}
