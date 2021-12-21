package com.honesty.post.controller;

import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.model.react.ReactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("post-api/reaction")
@AllArgsConstructor
@Tag(name = "reaction api")
public class ReactionController {

    private final ReactService reactService;

    @PostMapping("react/{postId}")
    @PreAuthorize("hasAuthority('comment:write')")
    public ResponseEntity<String> reactToPost(@RequestParam ReactEnum react, @PathVariable String postId, HttpServletRequest request){

        reactService.addOrRemoveReact(react, postId, getUserUid(request));
        return ResponseEntity.ok("react added");
    }

    private UUID getUserUid(HttpServletRequest request){
        String username = String.valueOf(request.getAttribute("username"));
        if(!username.equals("null"))
            return UUID.fromString(username);
        return null;
    }


}
