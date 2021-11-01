package com.honesty.post.controller;

import com.honesty.post.controller.dto.SubCommentReqDto;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.service.UserService;
import com.honesty.post.subcomment_model.react.ReactSubCommentService;
import com.honesty.post.subcomment_model.report.ReportSubCommentService;
import com.honesty.post.subcomment_model.subcomment.SubCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("post-api/sub-comment")
@AllArgsConstructor
@Tag(name = "sub comment api")
public class SubCommentController {

    private final SubCommentService subCommentService;
    private final UserService userService;
    private final ReportSubCommentService reportService;
    private final ReactSubCommentService reactService;

    @PostMapping()
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: comment not found")
    public ResponseEntity<String> addSubComment(@RequestBody @Valid SubCommentReqDto subComment, HttpServletRequest request){

        subCommentService.addSubCommentToComment(
                subComment.getContent(),
                subComment.getLocation(),
                subComment.getCommentUid(),
                userService.getUserUid(request)
        );
        return ResponseEntity.status(201).body("sub_comment added");
    }

    @PostMapping("ignore/{subCommentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub_comment not found\n406: not same user")
    public ResponseEntity<String> ignoreSubComment(@PathVariable String subCommentId, HttpServletRequest request){
        subCommentService.ignoreSubCommentOfUser(subCommentId, userService.getUserUid(request));
        return ResponseEntity.status(201).body("sub comment ignored");
    }

    @PostMapping("report/{subCommentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub_comment not found\n406: reported twice")
    public ResponseEntity<String> reportSubComment(@PathVariable String subCommentId,HttpServletRequest request){

        reportService.reportSubCommentOfUser(subCommentId, userService.getUserUid(request));
        return ResponseEntity.status(201).body("sub comment reported");
    }

    @PostMapping("react/{subCommentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub_comment not found")
    public ResponseEntity<String> reactToSubComment(
            @RequestParam ReactEnum react,
            @PathVariable String subCommentId,
            HttpServletRequest request
    ){

        reactService.addOrRemoveReact(subCommentId, react, userService.getUserUid(request));
        return ResponseEntity.status(201).body("react added");
    }

    @GetMapping("by-comment/{commentId}")
    @Operation(description = "400: bad request\n404: comment not found")
    public ResponseEntity<WrapperSubCommentResDto> getSubCommentsByComment(@PathVariable String commentId,
                                                                           @RequestParam Integer page,
                                                                           @RequestParam Integer size,
                                                                           HttpServletRequest request){
        WrapperSubCommentResDto subCommentsByCommentId = subCommentService.getSubCommentsByCommentId(
                commentId,
                page,
                size,
                userService.getUserUid(request)
        );
        return ResponseEntity.ok(subCommentsByCommentId);
    }
}
