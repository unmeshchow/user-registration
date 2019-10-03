package com.baeldung.registration.services;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.converters.UserCommandToUser;
import com.baeldung.registration.converters.UserToUserCommand;
import com.baeldung.registration.domain.User;
import com.baeldung.registration.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Created by uc on 10/1/2019
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCommandToUser userCommandToUser;
    private final UserToUserCommand userToUserCommand;

    public UserServiceImpl(UserRepository userRepository,
                           UserCommandToUser userCommandToUser,
                           UserToUserCommand userToUserCommand) {
        this.userRepository = userRepository;
        this.userCommandToUser = userCommandToUser;
        this.userToUserCommand = userToUserCommand;
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserCommand saveUser(UserCommand userCommand) {
        User userToSave =  userCommandToUser.convert(userCommand);
        userToSave.setRole("USER_ROLE");

        return userToUserCommand.convert(userRepository.save(userToSave));
    }
}
