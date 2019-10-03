package com.baeldung.registration.controllers;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Created by uc on 9/29/2019
 */
@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService registrationService) {
        this.userService = registrationService;
    }

    @GetMapping("/user/registration")
    public String showUserRegistrationForm(Model model) {
        model.addAttribute("userCommand", UserCommand.builder().build());
        return "userRegistrationForm";
    }

    @PostMapping("/user/registration")
    public String processUserRegistration(@Valid UserCommand userCommand, BindingResult result) {
        if (result.hasErrors()) {
            return "userRegistrationForm";
        }

        if (userService.isEmailExists(userCommand.getEmail())) {
            result.rejectValue("email", "EmailExists");
            return "userRegistrationForm";
        }

        UserCommand savedUserCommand = userService.saveUser(userCommand);

        return "redirect:/successRegister/" + savedUserCommand.getId();
    }
}
