package com.fixer.service.impl;

import com.fixer.model.Chat;
import com.fixer.model.Message;
import com.fixer.model.Project;
import com.fixer.model.User;
import com.fixer.repository.MessageRepository;
import com.fixer.repository.UserRepository;
import com.fixer.service.MessageService;
import com.fixer.service.ProjectService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(UserRepository userRepository,
                              ProjectService projectService,
                              MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.messageRepository = messageRepository;
    }

    @Override
    public Message sendMessage(Long senderId, Long projectId, String content) throws Exception {

        User user = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found with ID - " + senderId));

        Chat chat = projectService.getChatByProjectId(projectId).getProject().getChat();

        Message message = new Message();
        message.setContent(content);
        message.setSender(user);
        message.setCreatedAt(LocalDateTime.now());
        message.setChat(chat);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByProjectId(Long projectId) throws Exception {

        Chat chat = projectService.getChatByProjectId(projectId);

        return messageRepository.findByChatIdOrderByCreatedAtAsc(chat.getId());
    }
}
