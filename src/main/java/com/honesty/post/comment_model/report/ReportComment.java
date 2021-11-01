package com.honesty.post.comment_model.report;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honesty.post.comment_model.comment.Comment;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class ReportComment {

    public ReportComment(){
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
            name = "comment_id"
    )
    @JsonIgnore
    private Comment comment;
}
