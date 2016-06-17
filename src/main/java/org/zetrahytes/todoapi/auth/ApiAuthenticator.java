package org.zetrahytes.todoapi.auth;

import java.util.Optional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.zetrahytes.todoapi.entity.User;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class ApiAuthenticator implements Authenticator<BasicCredentials, User> {

    /*
     * Role-based Access Control
     *
     * admin has full access (all privileges)
     * user can get note by id, add, update and search notes
     * guest can just get note by id
     */
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
        AuthenticationToken token = new UsernamePasswordToken(credentials.getUsername(), credentials.getPassword());
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
            return Optional.of(new User(currentUser));
        } catch (AuthenticationException e) {
            return Optional.empty();
        }
    }

}
