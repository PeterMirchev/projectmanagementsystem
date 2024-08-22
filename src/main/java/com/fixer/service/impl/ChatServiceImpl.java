package com.fixer.service.impl;

import com.fixer.model.Chat;
import com.fixer.repository.ChatRepository;
import com.fixer.service.ChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat createChat(Chat chat) {

        return chatRepository.save(chat);
    }
}
