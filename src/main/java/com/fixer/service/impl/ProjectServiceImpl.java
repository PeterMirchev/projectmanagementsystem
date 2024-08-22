package com.fixer.service.impl;

import com.fixer.model.Chat;
import com.fixer.model.Project;
import com.fixer.model.User;
import com.fixer.repository.ProjectRepository;
import com.fixer.service.ChatService;
import com.fixer.service.ProjectService;
import com.fixer.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ChatService chatService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserService userService,
                              ChatService chatService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Project createProject(Project project, User user) throws Exception {

        Project createdProject = new Project();

        mapToCreatedProject(project, user, createdProject);

        Project persistedProject = projectRepository.save(createdProject);

        Chat chat = new Chat();
        chat.setProject(persistedProject);

        Chat projectChat = chatService.createChat(chat);
        persistedProject.setChat(projectChat);

        return persistedProject;
    }


    @Override
    public List<Project> getProjectByTeam(User user,
                                          String category,
                                          String tag) throws Exception {

        List<Project> projects = projectRepository.findByTeamContainingOrOwner(user, user);

        if (category != null) {
            projects = projects
                    .stream()
                    .filter(p -> p.getCategory().equals(category))
                    .collect(Collectors.toList());
        }

        if (tag != null) {
            projects = projects
                    .stream()
                    .filter(p -> p.getTags().contains(tag))
                    .collect(Collectors.toList());
        }

        return projects;
    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {

        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(String.format("Project not found. Invalid ID - %s", projectId)));
    }

    @Override
    public void deleteProject(Long projectId, Long userId) throws Exception {

        User userById = userService.findUserById(userId);

        if (userById == null) {
            throw  new RuntimeException(String.format("The user with ID - %s is not authorised to delete the project.", userId));
        }

        projectRepository.deleteById(projectId);

    }

    @Override
    public Project updateProject(Project updatedProject, Long id) throws Exception {

        Project project = getProjectById(id);

        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setTags(updatedProject.getTags());

        return projectRepository.save(project);
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws Exception {

        Project project = getProjectById(projectId);

        User user = userService.findUserById(userId);

        if (!project.getTeam().contains(user)) {
            project.getChat().getUsers().add(user);
            project.getTeam().add(user);
        }

        projectRepository.save(project);
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws Exception {

        Project project = getProjectById(projectId);

        User user = userService.findUserById(userId);

        if (project.getTeam().contains(user)) {
            project.getChat().getUsers().remove(user);
            project.getTeam().remove(user);
        }

        projectRepository.save(project);
    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {

        if (getProjectById(projectId) == null) {
            throw  new Exception(String.format("Invalid project ID - %s", projectId));
        }

        return getProjectById(projectId).getChat();
    }

    @Override
    public List<Project> searchProjects(String keyword, User user) {

        return projectRepository.findByNameContainingAndTeamContains(keyword, user);
    }

    protected static void mapToCreatedProject(Project project,
                                            User user,
                                            Project createdProject) {

        createdProject.setOwner(user);
        createdProject.setTags(project.getTags());
        createdProject.setName(project.getName());
        createdProject.setCategory(project.getCategory());
        createdProject.setDescription(project.getDescription());
        createdProject.getTeam().add(user);
    }
}
