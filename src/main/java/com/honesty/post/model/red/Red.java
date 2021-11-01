package com.honesty.post.model.red;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honesty.post.model.post.Post;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class Red {
    public Red(){
        createdAt = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private ZonedDateTime createdAt;

    private UUID userUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "post_id"
    )
    @JsonIgnore
    private Post post;
}
