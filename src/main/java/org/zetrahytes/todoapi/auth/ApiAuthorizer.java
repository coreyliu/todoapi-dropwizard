package org.zetrahytes.todoapi.auth;

import org.zetrahytes.todoapi.entity.User;

import io.dropwizard.auth.Authorizer;

public class ApiAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        if (user.getSubject() != null && user.getSubject().hasRole(role)) {
            return true;
        }

        return false;
    }

}
