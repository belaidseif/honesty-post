package com.honesty.post.controller;


import com.honesty.post.controller.dto.SubSubCommentReqDto;
import com.honesty.post.controller.dto.res.WrapperSubCommentResDto;
import com.honesty.post.controller.dto.res.WrapperSubSubCommentResDto;
import com.honesty.post.model.react.ReactEnum;
import com.honesty.post.service.UserService;
import com.honesty.post.subsubcomment_model.react.ReactSubSubCommentService;
import com.honesty.post.subsubcomment_model.report.ReportSubSubComment;
import com.honesty.post.subsubcomment_model.report.ReportSubSubCommentService;
import com.honesty.post.subsubcomment_model.subsubcomment.SubSubCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("post-api/sub-sub-comment")
@AllArgsConstructor
@Tag(name = "sub sub comment api")
public class SubSubCommentController {

    private final SubSubCommentService subSubCommentService;
    private final UserService userService;
    private final ReportSubSubCommentService reportService;
    private final ReactSubSubCommentService reactService;

    @PostMapping()
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub comment not found")
    public ResponseEntity<String> addSubSubComment(
            @RequestBody @Valid SubSubCommentReqDto subSubComment,
            HttpServletRequest request
    ){

        subSubCommentService.addSubSubCommentToComment(
                subSubComment.getContent(),
                subSubComment.getLocation(),
                subSubComment.getSubCommentUid(),
                userService.getUserUid(request)
        );
        return ResponseEntity.status(201).body("sub_sub_comment added");
    }

    @PostMapping("ignore/{subSubCommentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub_sub_comment not found\n406: not same user")
    public ResponseEntity<String> ignoreSubSubComment(@PathVariable String subSubCommentId, HttpServletRequest request){
        subSubCommentService.ignoreSubSubCommentOfUser(subSubCommentId, userService.getUserUid(request));
        return ResponseEntity.status(201).body("sub sub comment ignored");
    }

    @PostMapping("report/{subSubCommentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub_sub_comment not found\n406: reported twice")
    public ResponseEntity<String> reportSubSubComment(@PathVariable String subSubCommentId,HttpServletRequest request){

        reportService.reportSubSubCommentOfUser(subSubCommentId, userService.getUserUid(request));
        return ResponseEntity.status(201).body("sub sub comment reported");
    }

    @PostMapping("react/{subSubCommentId}")
    @PreAuthorize("hasAuthority('comment:write')")
    @Operation(description = "400: bad request\n404: sub_sub_comment not found")
    public ResponseEntity<String> reactToSubComment(
            @RequestParam ReactEnum react,
            @PathVariable String subSubCommentId,
            HttpServletRequest request
    ){

        reactService.addOrRemoveReact(subSubCommentId, react, userService.getUserUid(request));
        return ResponseEntity.status(201).body("react added");
    }

    @GetMapping("by-sub-comment/{subCommentId}")
    @Operation(description = "400: bad request\n404: sub comment not found")
    public ResponseEntity<WrapperSubSubCommentResDto> getSubSubCommentsBySubComment(
            @PathVariable String subCommentId,
            @RequestParam Integer page,
            @RequestParam Integer size,
            HttpServletRequest request
    ){
        WrapperSubSubCommentResDto subSubCommentsByCommentId = subSubCommentService
                .getSubSubCommentsBySubCommentId(
                        subCommentId,
                        page,
                        size,
                        userService.getUserUid(request)
        );
        return ResponseEntity.ok(subSubCommentsByCommentId);
    }
}
