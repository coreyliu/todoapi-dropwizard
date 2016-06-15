package org.zetrahytes.todoapi.auth;

import org.zetrahytes.todoapi.entity.User;

import io.dropwizard.auth.Authorizer;

public class ApiAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        if (user.getRoles() != null && user.getRoles().contains(role)) {
            return true;
        }

        return false;
    }

}
