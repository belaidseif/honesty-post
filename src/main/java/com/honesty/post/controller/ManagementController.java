package com.honesty.post.controller;

import com.honesty.post.model.red.RedService;
import com.honesty.post.model.report.ReportService;
import com.honesty.post.model.seen.SeenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("post-api/management")
@AllArgsConstructor
@Tag(name = "management api")
public class ManagementController {

    private final SeenService seenService;
    private final RedService redService;
    private final ReportService reportService;

    @PostMapping("seen/{postId}")
    public ResponseEntity<String> postSeenByUser(@PathVariable String postId, HttpServletRequest request){

        seenService.addSeenByUserToPost(postId, getUserUid(request));
        return ResponseEntity.status(HttpStatus.CREATED).body("post seen");
    }

    @PostMapping("red/{postId}")
    public ResponseEntity<String> postRedByUser(@PathVariable String postId, HttpServletRequest request){

        redService.addRedByUserToPost(postId, getUserUid(request));
        return ResponseEntity.status(HttpStatus.CREATED).body("post red");
    }

    @PostMapping("report/{postId}")
    public ResponseEntity<String> postReportedByUser(@PathVariable String postId, HttpServletRequest request){

        reportService.reportPost(postId, getUserUid(request));
        return ResponseEntity.status(HttpStatus.CREATED).body("post reported");
    }

    private UUID getUserUid(HttpServletRequest request){
        String username = String.valueOf(request.getAttribute("username"));
        System.out.println("username " + username);
        if(!username.equals("null"))
            return UUID.fromString(username);
        return null;
    }
}
