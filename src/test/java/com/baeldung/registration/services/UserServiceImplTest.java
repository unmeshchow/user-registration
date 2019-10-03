package com.baeldung.registration.services;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.converters.UserCommandToUser;
import com.baeldung.registration.converters.UserToUserCommand;
import com.baeldung.registration.domain.User;
import com.baeldung.registration.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToUserCommand userToUserCommand;

    @Mock
    private UserCommandToUser userCommandToUser;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void emailNotExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertThat(userService.isEmailExists(anyString())).isFalse();
        verify(userRepository).existsByEmail(anyString());
    }

    @Test
    public void emailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThat(userService.isEmailExists(anyString())).isTrue();
        verify(userRepository).existsByEmail(anyString());
    }

    @Test
    public void saveUser() {
        User user = User.builder().id(1L).build();
        UserCommand userCommand = UserCommand.builder().id(1L).build();
        when(userCommandToUser.convert(any(UserCommand.class))).thenReturn(user);
        when(userToUserCommand.convert(any(User.class))).thenReturn(userCommand);
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.saveUser(userCommand)).isNotNull();
        verify(userCommandToUser).convert(userCommand);
        verify(userToUserCommand).convert(user);
        verify(userRepository).save(user);

    }
}