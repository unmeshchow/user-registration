package com.baeldung.registration.services;

import com.baeldung.registration.commands.UserCommand;

/**
 * Created by uc on 10/1/2019
 */
public interface UserService {

    boolean isEmailExists(String email);

    UserCommand saveUser(UserCommand userCommand);
}
