package com.fixer.service;


import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

public interface EmailService {

    void sendEmailWithToken(String userEmail, String link) throws MessagingException, MailException;
}
