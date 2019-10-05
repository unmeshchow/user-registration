package com.baeldung.registration.controllers;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.converters.UserCommandToUser;
import com.baeldung.registration.domain.User;
import com.baeldung.registration.domain.VerificationToken;
import com.baeldung.registration.events.OnRegistrationCompleteEvent;
import com.baeldung.registration.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

/**
 * Created by uc on 9/29/2019
 */
@Slf4j
@Controller
public class RegistrationController {

    private final UserService userService;
    private final UserCommandToUser userCommandToUser;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MessageSource messageSource;

    public RegistrationController(UserService registrationService,
                                  UserCommandToUser userCommandToUser,
                                  ApplicationEventPublisher applicationEventPublisher,
                                  MessageSource messageSource) {
        this.userService = registrationService;
        this.userCommandToUser = userCommandToUser;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
    }

    @GetMapping("/user/registration")
    public String showUserRegistrationForm(Model model) {
        model.addAttribute("userCommand", UserCommand.builder().build());
        return "userRegistrationForm";
    }

    @PostMapping("/user/registration")
    public String processUserRegistration(@Valid UserCommand userCommand,
                                          BindingResult result,
                                          WebRequest webRequest) {
        if (result.hasErrors()) {
            return "userRegistrationForm";
        }

        if (userService.isEmailExists(userCommand.getEmail())) {
            result.rejectValue("email", "EmailExists");
            return "userRegistrationForm";
        }

        UserCommand savedUserCommand = userService.registerUser(userCommand);

        try {
            OnRegistrationCompleteEvent onRegistrationCompleteEvent =
                new OnRegistrationCompleteEvent(userCommandToUser.convert(savedUserCommand),
                        webRequest.getContextPath(), webRequest.getLocale());
            applicationEventPublisher.publishEvent(onRegistrationCompleteEvent);
        } catch (Exception exc) {
            log.error("Error during sending email: " + exc);
            return "redirect:/emailError";
        }

        return "redirect:/successRegister/" + savedUserCommand.getId();
    }

    @GetMapping("/user/registration/confirm")
    public String confirmUserRegistration(@RequestParam("token") String token,
                                          WebRequest webRequest,
                                          Model model) {
        VerificationToken verificationToken = userService.getVerificationTokenByToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("confirmation.invalid.token.message",
                    null, webRequest.getLocale());
            model.addAttribute("message", message);
            return "redirect:/badUser";
        }

        if (verificationToken.isExpired()) {
            String message = messageSource.getMessage("confirmation.expired.token.message",
                    null, webRequest.getLocale());
            model.addAttribute("message", message);
            return "redirect:/badUser";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);

        userService.saveUser(user);

        return "redirect:/login";
    }
}
