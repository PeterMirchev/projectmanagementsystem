package com.fixer.controller;

import com.fixer.model.Chat;
import com.fixer.model.Message;
import com.fixer.request.CreateMessageRequest;
import com.fixer.service.MessageService;
import com.fixer.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final ProjectService projectService;

    public MessageController(MessageService messageService,
                             ProjectService projectService) {
        this.messageService = messageService;
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest message) throws Exception {

        Chat chat = projectService.getChatByProjectId(message.getProjectId()).getProject().getChat();

        if (chat == null) {
            throw new Exception("Chat not found.");
        }

        Message sentMessage = messageService.sendMessage(
                message.getSenderId(),
                message.getProjectId(),
                message.getContent());

        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable(name = "projectId") Long projectId) throws Exception {

        List<Message> messages = messageService.getMessagesByProjectId(projectId);

        return ResponseEntity.ok(messages);
    }
}
