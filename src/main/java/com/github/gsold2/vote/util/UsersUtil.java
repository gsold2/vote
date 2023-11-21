package com.github.gsold2.vote.util;

import com.github.gsold2.vote.model.Role;
import com.github.gsold2.vote.model.User;
import com.github.gsold2.vote.to.UserTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}