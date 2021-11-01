package com.honesty.post.subsubcomment_model.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honesty.post.subcomment_model.subcomment.SubComment;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubComment;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;


@Entity
@Data
public class ReportSubSubComment {

    public ReportSubSubComment(){
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
            name = "sub_sub_comment_id"
    )
    @JsonIgnore
    private SubSubComment subSubComment;
}
