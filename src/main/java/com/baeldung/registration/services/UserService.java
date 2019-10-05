package com.baeldung.registration.services;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.domain.User;
import com.baeldung.registration.domain.VerificationToken;

/**
 * Created by uc on 10/1/2019
 */
public interface UserService {

    boolean isEmailExists(String email);

    UserCommand registerUser(UserCommand userCommand);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationTokenByToken(String token);

    void saveUser(User user);
}
