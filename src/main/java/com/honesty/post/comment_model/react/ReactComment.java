package com.honesty.post.comment_model.react;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honesty.post.comment_model.comment.Comment;

import com.honesty.post.model.react.ReactEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class ReactComment {

    public ReactComment(){
        createdAt = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ReactEnum react;

    private ZonedDateTime createdAt;

    private UUID userUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "comment_id"
    )
    @JsonIgnore
    private Comment comment;


}
