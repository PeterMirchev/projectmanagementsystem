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

import static com.fixer.config.BaseMessagesAndPaths.*;

@RestController
@RequestMapping(ISSUE_CONTROLLER_BASE_PATH)
public class IssueController {

    private final IssueService issueService;

    private final UserService userService;

    public IssueController(IssueService issueService,
                           UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
    }

    @GetMapping(ISSUE_ID)
    public ResponseEntity<Issue> getIssueById(@PathVariable(name = "issueId") Long issueId) throws Exception {

        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }

    @GetMapping(PROJECT_PROJECT_ID)
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable(name = "projectId") Long projectId) throws Exception {

        return ResponseEntity.ok(issueService.getIssueByProjectId(projectId));
    }


    @PostMapping
    public ResponseEntity<IssueDTO> createIssue(@RequestBody IssueRequest issue,
                                                @RequestHeader(value = "Authorization", required=false) String token) throws Exception {


        User user = userService.findUserProfileByJwt(token);

        Issue createdIssue = issueService.createIssue(issue, user);

        IssueDTO issueDTO = mapToIssueDto(createdIssue);

        return ResponseEntity.ok(issueDTO);

    }

    @DeleteMapping(ISSUE_ID)
    public ResponseEntity<MessageResponse> deleteIssue(@PathVariable("issueId") Long issueId,
                                                       @RequestHeader(value = "Authorization", required = false) String token) throws Exception {

        User user = userService.findUserProfileByJwt(token);
        issueService.deleteIssue(issueId, user.getId());

        MessageResponse response = new MessageResponse();
        response.setMessageResponse(DELETED_ISSUE);

        return ResponseEntity.ok(response);
    }

    @GetMapping(SEARCH)
    public ResponseEntity<List<Issue>> searchIssues() {

        return null;
    }

    @PutMapping(ISSUE_ASSIGNEE_USER_ID)
    public ResponseEntity<Issue> addUserToIssue(@PathVariable(name = "issueId") Long issueId,
                                                @PathVariable(name = "userId") Long userId) throws Exception {

        System.out.println();
        Issue issue = issueService.addUserToIssue(issueId, userId);

        return ResponseEntity.ok(issue);

    }

    @PutMapping(ISSUE_STATUS)
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
