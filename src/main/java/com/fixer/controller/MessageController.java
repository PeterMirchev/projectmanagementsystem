package com.fixer.controller;

import com.fixer.model.Chat;
import com.fixer.model.Message;
import com.fixer.request.CreateMessageRequest;
import com.fixer.service.MessageService;
import com.fixer.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fixer.config.BaseMessagesAndPaths.*;

@RestController
@RequestMapping(MESSAGE_CONTROLLER_BASE_PATH)
public class MessageController {

    private final MessageService messageService;
    private final ProjectService projectService;

    public MessageController(MessageService messageService,
                             ProjectService projectService) {

        this.messageService = messageService;
        this.projectService = projectService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest message) throws Exception {

        Chat chat = projectService.getChatByProjectId(message.getProjectId()).getProject().getChat();

        if (chat == null) {
            throw new Exception(CHAT_NOT_FOUND);
        }

        Message sentMessage = messageService.sendMessage(
                message.getSenderId(),
                message.getProjectId(),
                message.getContent());

        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping(CHAT_PROJECT_ID)
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable(name = "projectId") Long projectId) throws Exception {

        List<Message> messages = messageService.getMessagesByProjectId(projectId);

        return ResponseEntity.ok(messages);
    }
}
