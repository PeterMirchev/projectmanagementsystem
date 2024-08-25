package com.fixer.controller;

import com.fixer.model.Comment;
import com.fixer.model.User;
import com.fixer.request.CreateCommentRequest;
import com.fixer.response.MessageResponse;
import com.fixer.service.CommentService;
import com.fixer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fixer.config.BaseMessagesAndPaths.*;

@RestController
@RequestMapping(COMMENT_CONTROLLER_BASE_PATH)
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    public CommentController(CommentService commentService,
                             UserService userService) {

        this.commentService = commentService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CreateCommentRequest commentRequest,
                                                 @RequestHeader(value = "Authorization", required = false) String token) throws Exception {

        User user = userService.findUserProfileByJwt(token);

        Comment comment = commentService.createComment(
                commentRequest.getIssueId(),
                user.getId(),
                commentRequest.getContent());

        return ResponseEntity.ok(comment);
    }

    @DeleteMapping(COMMENT_ID)
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable(name = "commentId") Long commentId,
                                                         @RequestHeader(value = "Authorization", required = false) String token) throws Exception {

        User user = userService.findUserProfileByJwt(token);
        commentService.deleteComment(commentId, user.getId());

        MessageResponse response = new MessageResponse();
        response.setMessageResponse(COMMENT_DELETED);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(ISSUE_ID)
    public ResponseEntity<List<Comment>> getCommentsByIssueId(@PathVariable(name = "issueId") Long issueId) {

        List<Comment> comments = commentService.findCommentsByIssueId(issueId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
