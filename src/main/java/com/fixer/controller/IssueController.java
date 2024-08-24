package com.fixer.controller;

import com.fixer.model.Issue;
import com.fixer.model.User;
import com.fixer.model.issueDTO.IssueDTO;
import com.fixer.request.IssueRequest;
import com.fixer.response.MessageResponse;
import com.fixer.service.IssueService;
import com.fixer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    private final UserService userService;

    public IssueController(IssueService issueService,
                           UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable(name = "issueId") Long issueId) throws Exception {

        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable(name = "projectId") Long projectId) throws Exception {

        return ResponseEntity.ok(issueService.getIssueByProjectId(projectId));
    }


    @PostMapping//TODO:fix the recursion on the output
    public ResponseEntity<IssueDTO> createIssue(@RequestBody IssueRequest issue,
                                                @RequestHeader(value = "Authorization", required=false) String token) throws Exception {


        User user = userService.findUserProfileByJwt(token);

        Issue createdIssue = issueService.createIssue(issue, user);

        IssueDTO issueDTO = mapToIssueDto(createdIssue);

        return ResponseEntity.ok(issueDTO);

    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<MessageResponse> deleteIssue(@PathVariable("issueId") Long issueId,
                                                       @RequestHeader(value = "Authorization", required = false) String token) throws Exception {

        User user = userService.findUserProfileByJwt(token);
        issueService.deleteIssue(issueId, user.getId());

        MessageResponse response = new MessageResponse();
        response.setMessageResponse("Issue deleted successfully.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Issue>> searchIssues() {

        return null;
    }

    @PutMapping("{issueId}/assignee/{userId}")
    public ResponseEntity<Issue> addUserToIssue(@PathVariable(name = "issueId") Long issueId,
                                                @PathVariable(name = "userId") Long userId) throws Exception {

        Issue issue = issueService.addUserToIssue(issueId, userId);

        return ResponseEntity.ok(issue);

    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issue> updateStatus(@PathVariable(name = "issueId") Long issueId,
                                              @PathVariable(name = "status") String status) throws Exception {

        Issue issue = issueService.updateStatus(issueId, status);

        return ResponseEntity.ok(issue);
    }

    protected static IssueDTO mapToIssueDto(Issue createdIssue) {

        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setDescription(createdIssue.getDescription());
        issueDTO.setDueDate(createdIssue.getDueDate());
        issueDTO.setId(createdIssue.getId());
        issueDTO.setPriority(createdIssue.getPriority());
        issueDTO.setProject(createdIssue.getProject());
        issueDTO.setProjectId(createdIssue.getProjectId());
        issueDTO.setStatus(createdIssue.getStatus());
        issueDTO.setTitle(createdIssue.getTitle());
        issueDTO.setTags(createdIssue.getTags());
        issueDTO.setAssignee(createdIssue.getAssignee());
        return issueDTO;
    }


}
