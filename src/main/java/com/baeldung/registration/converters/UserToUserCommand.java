package com.baeldung.registration.converters;

import com.baeldung.registration.commands.UserCommand;
import com.baeldung.registration.domain.User;
import lombok.Synchronized;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Created by uc on 10/2/2019
 */
@Component
public class UserToUserCommand implements Converter<User, UserCommand> {

    @Synchronized
    @Nullable
    @Override
    public UserCommand convert(User source) {
        if (source == null) {
            return null;
        }

        final UserCommand target = UserCommand.builder().build();
        BeanUtils.copyProperties(source, target);

        return target;
    }
}
