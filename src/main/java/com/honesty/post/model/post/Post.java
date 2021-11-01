package com.honesty.post.model.post;

import com.honesty.post.comment_model.comment.Comment;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Post {

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


}
