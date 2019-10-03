package com.baeldung.registration.commands;

import com.baeldung.registration.validators.PasswordMatches;
import com.baeldung.registration.validators.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Created by uc on 9/29/2019
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class UserCommand {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    private String password;
    private String matchingPassword;
}
