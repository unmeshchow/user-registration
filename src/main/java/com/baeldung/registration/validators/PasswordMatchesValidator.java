package com.baeldung.registration.validators;

import com.baeldung.registration.commands.UserCommand;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by uc on 9/30/2019
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        UserCommand userCommand = (UserCommand) object;
        return userCommand.getPassword().equals(userCommand.getMatchingPassword());
    }
}
