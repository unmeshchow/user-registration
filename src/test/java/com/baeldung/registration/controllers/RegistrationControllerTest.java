package com.baeldung.registration.controllers;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.converters.UserCommandToUser;
import com.baeldung.registration.domain.User;
import com.baeldung.registration.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserCommandToUser userCommandToUser;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    public void showUserRegistrationForm() throws Exception {
        mockMvc.perform(get("/user/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("userRegistrationForm"))
                .andExpect(model().attributeExists("userCommand"));

        verifyZeroInteractions(userService);
    }

    @Test
    public void processUserRegistrationAllFieldsEmpty() throws Exception {
        mockMvc.perform(post("/user/registration")
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "")
                .param("password", "")
                .param("matchingPassword", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userCommand"))
                .andExpect(model().attributeHasFieldErrors("userCommand", "firstName"))
                .andExpect(model().attributeHasFieldErrors("userCommand", "lastName"))
                .andExpect(model().attributeHasFieldErrors("userCommand", "email"))
                .andExpect(model().attributeHasFieldErrors("userCommand", "password"))
                .andExpect(view().name("userRegistrationForm"));

        verifyZeroInteractions(userService);
    }

    @Test
    public void processUserRegistrationInvalidEmail() throws Exception {
        mockMvc.perform(post("/user/registration")
                .param("firstName", "Unmesh")
                .param("lastName", "Chowdhury")
                .param("email", "unmeshchow@gmailcom")
                .param("password", "password")
                .param("matchingPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userCommand"))
                .andExpect(model().attributeHasFieldErrors("userCommand", "email"))
                .andExpect(view().name("userRegistrationForm"));

        verifyZeroInteractions(userService);
    }

    @Test
    public void processUserRegistrationPasswordMismatch() throws Exception {
        mockMvc.perform(post("/user/registration")
                .param("firstName", "Unmesh")
                .param("lastName", "Chowdhury")
                .param("email", "unmeshchow@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "passw"))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(view().name("userRegistrationForm"));

        verifyZeroInteractions(userService);
    }

    @Test
    public void processUserRegistrationEmailExists() throws Exception {
        when(userService.isEmailExists(anyString())).thenReturn(true);

        mockMvc.perform(post("/user/registration")
                .param("firstName", "Unmesh")
                .param("lastName", "Chowdhury")
                .param("email", "unmeshchow@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userCommand"))
                .andExpect(model().attributeHasFieldErrors("userCommand", "email"))
                .andExpect(view().name("userRegistrationForm"));

        verify(userService).isEmailExists("unmeshchow@gmail.com");
    }


    @Test
    public void processUserRegistrationEmailFail() throws Exception {
        UserCommand userCommand = UserCommand.builder().id(1L).build();
        when(userService.isEmailExists(anyString())).thenReturn(false);
        when(userService.registerUser(any(UserCommand.class))).thenReturn(userCommand);
        when(userCommandToUser.convert(any(UserCommand.class))).thenReturn(User.builder().build());
        doThrow(RuntimeException.class).when(applicationEventPublisher)
                .publishEvent(any(ApplicationEvent.class));

        mockMvc.perform(post("/user/registration")
                .param("firstName", "Unmesh")
                .param("lastName", "Chowdhury")
                .param("email", "unmeshchow@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/emailError"));

        verify(userService).isEmailExists("unmeshchow@gmail.com");
        verify(userService).registerUser(any(UserCommand.class));
        verify(userCommandToUser).convert(any(UserCommand.class));
        verify(applicationEventPublisher).publishEvent(any());
    }

    @Test
    public void processUserRegistration() throws Exception {
        UserCommand userCommand = UserCommand.builder().id(1L).build();
        when(userService.isEmailExists(anyString())).thenReturn(false);
        when(userService.registerUser(any(UserCommand.class))).thenReturn(userCommand);
        when(userCommandToUser.convert(any(UserCommand.class))).thenReturn(User.builder().build());
        doNothing().when(applicationEventPublisher).publishEvent(any(ApplicationEvent.class));

        mockMvc.perform(post("/user/registration")
                .param("firstName", "Unmesh")
                .param("lastName", "Chowdhury")
                .param("email", "unmeshchow@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/successRegister/1"));

        verify(userService).isEmailExists("unmeshchow@gmail.com");
        verify(userService).registerUser(any(UserCommand.class));
        verify(userCommandToUser).convert(any(UserCommand.class));
        verify(applicationEventPublisher).publishEvent(any());
    }
}