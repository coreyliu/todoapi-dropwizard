package org.zetrahytes.todoapi.auth;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.zetrahytes.todoapi.entity.User;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class ApiAuthenticator implements Authenticator<BasicCredentials, User> {

    private static final Map<String, String> USER_CREDENTIAL_MAPPING = ImmutableMap.of(
            "guest", "guest",
            "admin", "secretpwd"
        );
    private static final Map<String, Set<String>> USER_ROLES_MAPPING = ImmutableMap.of(
            "guest", ImmutableSet.of("USER"),
            "admin", ImmutableSet.of("ADMIN", "USER")
        );

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (USER_CREDENTIAL_MAPPING.containsKey(credentials.getUsername())) {
            final String password = USER_CREDENTIAL_MAPPING.get(credentials.getUsername());
            if (credentials.getPassword().equals(password)) {
                return Optional.of(
                        new User(credentials.getUsername(), USER_ROLES_MAPPING.get(credentials.getUsername())));
            }
        }

        return Optional.empty();
    }

}
