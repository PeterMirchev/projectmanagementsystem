package com.fixer.controller;

import com.fixer.model.Chat;
import com.fixer.model.Invitation;
import com.fixer.model.Project;
import com.fixer.model.User;
import com.fixer.request.InvitationRequest;
import com.fixer.response.MessageResponse;
import com.fixer.service.InvitationService;
import com.fixer.service.ProjectService;
import com.fixer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final InvitationService invitationService;

    public ProjectController(ProjectService projectService,
                             UserService userService,
                             InvitationService invitationService) {
        this.projectService = projectService;
        this.userService = userService;
        this.invitationService = invitationService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(@RequestParam(required = false) String category,
                                                     @RequestParam(required = false) String tag,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        List<Project> projects = projectService.getProjectByTeam(user, category, tag);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable(name = "projectId") Long projectId,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Project projects = projectService.getProjectById(projectId);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Project createdProject = projectService.createProject(project, user);

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }


    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId,
                                                 @RequestHeader("Authorization") String jwt,
                                                 @RequestBody Project project) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Project updatedProject = projectService.updateProject(project, projectId);

        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteAProject(@PathVariable(name = "projectId") Long projectId,
                                                          @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId, user.getId());

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessageResponse(String.format("Project with ID - %s successfully deleted.", projectId));

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam(name = "keyword", required = false) String keyword,
                                                        @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        List<Project> projects = projectService.searchProjects(keyword, user);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<Chat> getChatByProjectId(@PathVariable(name = "projectId") Long projectId,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Chat chat = projectService.getChatByProjectId(projectId);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("invite")
    public ResponseEntity<MessageResponse> inviteProject(@RequestBody InvitationRequest invite,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        invitationService.sendInvitation(invite.getEmail(), invite.getProjectId());

        MessageResponse response = new MessageResponse("User invitation sent.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("accept-invitation")
    public ResponseEntity<Invitation> acceptInviteProject(@RequestParam String token,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Invitation invitation = invitationService.acceptInvitation(token, user.getId());
        projectService.addUserToProject(invitation.getProjectId(), user.getId());

        return new ResponseEntity<>(invitation, HttpStatus.ACCEPTED);
    }
}
