package com.baeldung.registration.listeners;

import com.baeldung.registration.domain.User;
import com.baeldung.registration.events.OnRegistrationCompleteEvent;
import com.baeldung.registration.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by uc on 10/3/2019
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService userService;
    private final MessageSource messageSource;
    private final JavaMailSender javaMailSender;

    public RegistrationListener(UserService userService,
                                MessageSource messageSource,
                                JavaMailSender javaMailSender) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = messageSource.getMessage("confirmation.email.subject",
                null, event.getLocale());
        String confirmationUrl = event.getAppUrl() + "/user/registration/confirm?token=" + token;
        String message = messageSource.getMessage("confirmation.email.message",
                null, event.getLocale());
        String text = message + " - " + "http://localhost:8080" + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(text);

        javaMailSender.send(email);
    }
}
