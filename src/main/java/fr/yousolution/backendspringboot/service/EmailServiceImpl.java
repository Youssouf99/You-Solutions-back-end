package fr.yousolution.backendspringboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender javaMailSender;

    public void sendContactNotification(String name, String email, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("contact@you-solutions.fr");
        simpleMailMessage.setTo("contact@you-solutions.fr");
        simpleMailMessage.setSubject("New Contact Form Submission");
        simpleMailMessage.setText(String.format(
                "New contact form submission:\n\nName: %s\nEmail: %s\n\nMessage:\n%s",
                name, email, message
        ));

        javaMailSender.send(simpleMailMessage);
    }
}
