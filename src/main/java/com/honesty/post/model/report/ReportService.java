package com.honesty.post.model.report;

import com.honesty.post.model.post.Post;
import com.honesty.post.model.post.PostService;
import com.honesty.post.model.red.Red;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportService {
    private final PostService postService;
    private final ReportRepo repo;
    public void reportPost(String postId, UUID userUid) {
        Post post = postService.getPostById(postId);
        Report report = new Report();
        report.setPost(post);
        report.setUserUid(userUid);
        if(!repo.existsReportByPostIdAndUserUid(post.getId(), userUid))
            repo.save(report);
    }
}
