package com.rockskay.backend.infrastructure.mail;


public interface MailService {

    void send(
            String recipient,
            String subject,
            String body
    );
}
