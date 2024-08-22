package com.fixer.service.impl;

import com.fixer.model.Issue;
import com.fixer.model.Project;
import com.fixer.model.User;
import com.fixer.repository.IssueRepository;
import com.fixer.request.IssueRequest;
import com.fixer.service.IssueService;
import com.fixer.service.ProjectService;
import com.fixer.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public IssueServiceImpl(IssueRepository issueRepository,
                            ProjectService projectService,
                            UserService userService) {
        this.issueRepository = issueRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public Issue getIssueById(Long issueId) throws Exception {

        Optional<Issue> issue = issueRepository.findById(issueId);

        if (issue.isEmpty()) {
            throw new Exception("Invalid issue ID");
        }
        return issue.get();
    }

    @Override
    public List<Issue> getIssueByProjectId(Long projectId) throws Exception {

        return issueRepository.findByProjectId(projectId);
    }

    @Override
    public Issue createIssue(IssueRequest issueRequest, User user) throws Exception {

        Project project = projectService.getProjectById(issueRequest.getProjectId());

        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDescription(issueRequest.getDescription());
        issue.setStatus(issueRequest.getStatus());
        issue.setProjectId(issueRequest.getProjectId());
        issue.setPriority(issueRequest.getPriority());
        issue.setDueDate(issueRequest.getDueDate());

        issue.setProject(project);

        return issueRepository.save(issue);
    }

    @Override
    public void deleteIssue(Long issueId, Long userId) throws Exception {

        getIssueById(issueId);
        issueRepository.deleteById(issueId);
    }

    @Override
    public Issue addUserToIssue(Long issueId, Long userId) throws Exception {

        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);

        issue.setAssignee(user);

        return issueRepository.save(issue);
    }

    @Override
    public Issue updateStatus(Long issueId, String status) throws Exception {

        Issue issue = getIssueById(issueId);
        issue.setStatus(status);

        return issueRepository.save(issue);
    }
}
