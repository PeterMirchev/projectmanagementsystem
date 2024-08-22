package com.fixer.service.impl;

import com.fixer.model.Invitation;
import com.fixer.repository.InvitationRepository;
import com.fixer.service.EmailService;
import com.fixer.service.InvitationService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    public InvitationServiceImpl(InvitationRepository invitationRepository,
                                 EmailService emailService) {
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendInvitation(String email, Long projectId) throws MessagingException {

        String invitationToken = UUID.randomUUID().toString();

        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setProjectId(projectId);
        invitation.setToken(invitationToken);

        invitationRepository.save(invitation);

        String invitationLink = "http://localhost:5173/accept_invitation?token=" + invitationToken;
        emailService.sendEmailWithToken(email, invitationToken);
    }

    @Override
    public Invitation acceptInvitation(String token, Long userId) throws Exception {

        Invitation invitation = invitationRepository.findByToken(token);

        if (invitation == null) {
            throw new Exception("Invalid invitation token.");
        }

        return invitation;
    }

    @Override
    public String getTokenByUserMail(String userEmail) {

        Invitation invitation = invitationRepository.findByEmail(userEmail);

        return invitation.getToken();
    }

    @Override
    public void deleteToken(String token) {

        invitationRepository.deleteByToken(token);
    }
}
